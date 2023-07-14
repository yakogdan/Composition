package com.yakogdan.composition.presentation.fragments

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.yakogdan.composition.databinding.FragmentGameBinding
import com.yakogdan.composition.domain.entities.GameResult
import com.yakogdan.composition.presentation.viewmodelfactory.GameViewModelFactory
import com.yakogdan.composition.presentation.viewmodels.GameViewModel

class GameFragment : Fragment() {

    private val args by navArgs<GameFragmentArgs>()

    private val viewModelFactory by lazy {
        GameViewModelFactory(args.level, requireActivity().application)
    }

    private val viewModel: GameViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[GameViewModel::class.java]
    }

    private val tvOptions by lazy {
        mutableListOf<TextView>().apply {
            add(binding.tvOption1)
            add(binding.tvOption2)
            add(binding.tvOption3)
            add(binding.tvOption4)
            add(binding.tvOption5)
            add(binding.tvOption6)
        }
    }

    private var _binding: FragmentGameBinding? = null
    private val binding: FragmentGameBinding
        get() = _binding ?: throw RuntimeException("FragmentGameBinding == null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        setClickListenersToOptions()
    }

    private fun setClickListenersToOptions() {
        for (tvOption in tvOptions) {
            tvOption.setOnClickListener {
                viewModel.chooseAnswer(tvOption.text.toString().toInt())
            }
        }
    }

    private fun observeViewModel() {
        with(viewModel) {
            questionLD.observe(viewLifecycleOwner) {
                with(binding) {
                    tvSum.text = it.sum.toString()
                    tvLeftNumber.text = it.visibleNumber.toString()
                }
                for (i in 0 until tvOptions.size) {
                    tvOptions[i].text = it.options[i].toString()
                }
            }

            percentOfRightAnswerLD.observe(viewLifecycleOwner) {
                binding.progressBar.setProgress(it, true)
            }

            enoughCountOfRightAnswersLD.observe(viewLifecycleOwner) {
                binding.tvAnswersProgress.setTextColor(getColorByState(it))
            }

            enoughPercentOfRightAnswersLD.observe(viewLifecycleOwner) {
                binding.progressBar.progressTintList = ColorStateList.valueOf(getColorByState(it))
            }

            formattedTimeLD.observe(viewLifecycleOwner) {
                binding.tvTimer.text = it
            }

            minPercentOfRightAnswersLD.observe(viewLifecycleOwner) {
                binding.progressBar.secondaryProgress = it
            }

            gameResultLD.observe(viewLifecycleOwner) {
                launchGameFinishedFragment(it)
            }

            progressAnswersLD.observe(viewLifecycleOwner) {
                binding.tvAnswersProgress.text = it
            }
        }
    }

    private fun getColorByState(goodState: Boolean): Int {
        val colorResId = if (goodState) {
            android.R.color.holo_green_light
        } else {
            android.R.color.holo_red_light
        }
        return ContextCompat.getColor(requireContext(), colorResId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun launchGameFinishedFragment(gameResult: GameResult) {
        findNavController().navigate(
            GameFragmentDirections.actionGameFragmentToGameFinishedFragment(
                gameResult
            )
        )
    }
}