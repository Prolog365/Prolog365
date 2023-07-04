package com.example.prolog365.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prolog365.R
import com.example.prolog365.databinding.FragmentCalendarBinding
import com.example.prolog365.databinding.ScheduleViewBinding
import com.example.prolog365.db.PhonebookDB
import com.example.prolog365.db.ScheduleDB
import com.example.prolog365.db.ScheduleEntity
import com.example.prolog365.ui.Schedule.ScheduleView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDate

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
        ScheduleView.scheduleBinding = ScheduleViewBinding.inflate(inflater, null, false)
        val adapter = CalendarAdapter(eventList)
        binding.eventRecyclerView.adapter = adapter
        binding.eventRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val calendarView: CalendarView = root.findViewById(R.id.calendar_view)
        val calendarTextBar: TextView = root.findViewById(R.id.calendar_text_bar)

        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val selectedDate = LocalDate.of(year, month + 1, dayOfMonth) // Note: month is zero-based
            calendarTextBar.text = selectedDate.toString() + " 일정"

            // Call the function to load events based on the selected date
            loadEvents(selectedDate)
        }


        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fab.setOnClickListener{ v ->
            val bottomSheetDialog = AddCalendar()
            bottomSheetDialog.show(parentFragmentManager, "bottomSheet")
        }

        val selectedDate = LocalDate.now()
        binding.calendarTextBar.text = selectedDate.toString() + " 일정"
        loadEvents(selectedDate)
    }
    private fun loadEvents(date: LocalDate) {
        GlobalScope.launch(Dispatchers.IO) {
            val scheduleList = ScheduleDB.getScheduleWithDate(date)
            scheduleList?.let {
                val calendarDataList = it.map { scheduleEntity ->
                    CalendarData(
                        scheduleEntity.scheduleName,
                        LocalDate.parse(scheduleEntity.date),
                        scheduleEntity.phoneNumber,
                        scheduleEntity.picture
                    )
                }
                eventList.clear()
                eventList.addAll(calendarDataList)
                requireActivity().runOnUiThread {
                    binding.eventRecyclerView.adapter?.notifyDataSetChanged()
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}