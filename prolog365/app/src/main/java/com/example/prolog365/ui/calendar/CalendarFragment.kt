package com.example.prolog365.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prolog365.R
import com.example.prolog365.databinding.FragmentCalendarBinding

class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!
    private lateinit var fab: View

    val eventList = ArrayList<CalendarData>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        val root: View = binding.root
        fab = root.findViewById(R.id.calender_floating_action_button)

        eventList.add(CalendarData("s", "행사1", "행사행사"))
        eventList.add(CalendarData("s", "행사2", "행사행사"))
        eventList.add(CalendarData("s", "행사3", "행사행사"))
        eventList.add(CalendarData("s", "행사4", "행사행사"))
        eventList.add(CalendarData("s", "행사5", "행사행사"))
        eventList.add(CalendarData("s", "행사6", "행사행사"))
        eventList.add(CalendarData("s", "행사7", "행사행사"))

        binding.eventRecyclerView.adapter = CalendarAdapter(eventList)
        binding.eventRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fab.setOnClickListener{ v ->
            val bottomSheetDialog = AddCalendar()
            bottomSheetDialog.show(parentFragmentManager, "bottomSheet")
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}