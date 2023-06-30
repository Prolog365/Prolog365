package com.example.prolog365.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prolog365.databinding.FragmentCalendarBinding

class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    val eventList = ArrayList<CalendarData>()

    //private lateinit var calenderViewAdapter: CalendarRecyclerViewAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val calendarViewModel = ViewModelProvider(this).get(CalendarViewModel::class.java)
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)

        val root: View = binding.root

        eventList.add(CalendarData("s", "행사1", "행사행사"))
        eventList.add(CalendarData("s", "행사2", "행사행사"))
        eventList.add(CalendarData("s", "행사3", "행사행사"))
        eventList.add(CalendarData("s", "행사4", "행사행사"))
        eventList.add(CalendarData("s", "행사5", "행사행사"))
        eventList.add(CalendarData("s", "행사6", "행사행사"))
        eventList.add(CalendarData("s", "행사7", "행사행사"))

        binding.eventRecyclerView.adapter = CalendarAdapter(eventList)
        if(container!=null){
            binding.eventRecyclerView.layoutManager = LinearLayoutManager(container.context)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}