package com.capstone.berkebunplus.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.capstone.berkebunplus.R
import com.capstone.berkebunplus.data.Result
import com.capstone.berkebunplus.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val factory = HomeViewModelFactory.getInstance()
        val viewModel: HomeViewModel by viewModels { factory }

        viewModel.weatherData.observe(viewLifecycleOwner) { results->
            when (results) {
                is Result.Loading -> { binding.progressIndicator.visibility = View.VISIBLE}
                is Result.Success -> {
                    val weather = results.data
                    binding.progressIndicator.visibility = View.GONE
                    binding.tvCity.text = getString(R.string.result_info_city_country, weather.name, weather.sys.country)
                    binding.tvInfoDescriptionWeather.text = weather.weather[0].description
                    binding.tvInfoHumidityPercentage.text = getString(R.string.result_info_humidity, weather.main.humidity)
                    binding.tvInfoWindSpeed.text = getString(R.string.result_info_wind_speed, weather.wind.speed)
                    binding.tvInfoTemperature.text = getString(R.string.result_info_temperature, weather.main.temp)
                    binding.tvInfoPressure.text = getString(R.string.result_info_pressure, weather.main.pressure)
                }
                is Result.Error -> {
                    binding.progressIndicator.visibility = View.GONE
                }
            }
        }
        viewModel.fetchWeather()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}