package app.igormatos.botaprarodar.presentation.main


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.domain.model.DashboardInformation
import app.igormatos.botaprarodar.domain.model.Item
import app.igormatos.botaprarodar.domain.model.User
import app.igormatos.botaprarodar.data.local.SharedPreferencesModule
import app.igormatos.botaprarodar.data.network.firebase.FirebaseHelper
import app.igormatos.botaprarodar.data.network.RequestListener
import app.igormatos.botaprarodar.databinding.FragmentDashboardBinding
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.gms.tasks.Task
import com.google.firebase.functions.FirebaseFunctions
import com.google.gson.GsonBuilder
import org.koin.android.ext.android.inject


class DashboardFragment : Fragment() {

    private val percentFormatter = object : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            return "${"%.1f".format(value)}%"
        }
    }

    private val tripsFormatter = object : ValueFormatter() {
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return months[value.toInt()]
        }
    }

    private val months = mutableListOf<String>()
    private var pieChartColors = listOf<Int>()
    private lateinit var functions: FirebaseFunctions
    private lateinit var communityId: String
    private val gendersEntries = mutableListOf<PieEntry>()
    private val bicyclesEntries = mutableListOf<PieEntry>()
    private val tripsEntries = mutableListOf<Entry>()
    private val usersList = mutableListOf<User>()
    private val bicycleList = mutableListOf<Bike>()

    private val preferencesModule: SharedPreferencesModule by inject()
    private val binding: FragmentDashboardBinding by lazy {
        FragmentDashboardBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        communityId = preferencesModule.getJoinedCommunity().id

        pieChartColors = listOf(
            ContextCompat.getColor(requireContext(), R.color.orange),
            ContextCompat.getColor(requireContext(), R.color.purple),
            ContextCompat.getColor(requireContext(), R.color.green),
            ContextCompat.getColor(requireContext(), R.color.red)
        )

        functions = FirebaseFunctions.getInstance()

        setTripsChart()
        setGenderPieChart()
        setAvailablePieChart()
        updateCharts()
    }

    private fun setTripsChart() {
        val lineDataSet = LineDataSet(tripsEntries, "Total de viagens")
        val lineData = LineData(lineDataSet)

        binding.tripsChart.apply {
            this.data = lineData
            description = null
            xAxis.apply {
                granularity = 1f
                valueFormatter = tripsFormatter
            }

        }
    }

    private fun updateTripsChart() {
        tripsEntries.clear()

        tripsEntries.apply {
            add(Entry(0f, 100f))
            add(Entry(1f, 200f))
        }

        months.clear()

        months.apply {
            add("julho")
            add("agosto")
        }

        binding.tripsChart.invalidate()
    }

    private fun updateCharts() {
        getDashboardInformation().addOnCompleteListener {
            it.result?.let { map ->
                val gson = GsonBuilder().create()
                val json = gson.toJson(map)
                gson.fromJson(json, DashboardInformation::class.java)?.also { dashInfo ->
                    binding.tripsCountLabel.text = getString(
                        R.string.trip_count_label,
                        dashInfo.tripsCount.toString()
                    )
                }

                updateTripsChart()
            }
        }
    }

    private fun getDashboardInformation(): Task<Map<String, Any>> {
        val data = hashMapOf(
            "communityId" to communityId
        )

        return functions
            .getHttpsCallable("getDashboardInformation")
            .call(data)
            .continueWith { task ->
                val result = task.result?.data as Map<String, Any>
                result
            }
    }

    private fun updateGenderChart() {
        val groupedUsers = usersList.groupBy { it.gender }
        val maleCount = groupedUsers[0]?.count() ?: 0
        val femaleCount = groupedUsers[1]?.count() ?: 0

        gendersEntries.clear()
        gendersEntries.add(PieEntry(maleCount.toFloat(), "Homem"))
        gendersEntries.add(PieEntry(femaleCount.toFloat(), "Mulher"))
        binding.genderChart.invalidate()
    }

    private fun setGenderPieChart() {
        FirebaseHelper.getUsers(communityId, false, object : RequestListener<Item> {

            override fun onChildChanged(result: Item) {
                usersList[usersList.indexOf(result)] = result as User
                updateGenderChart()
            }

            override fun onChildAdded(result: Item) {
                usersList.add(result as User)
                updateGenderChart()
            }

            override fun onChildRemoved(result: Item) {
                usersList.remove(result as User)
                updateGenderChart()
            }

        })


        val pieDataSet = PieDataSet(gendersEntries, "Proporção de gênero").apply {
            colors = pieChartColors
            valueTextSize = 14f
        }

        val pieData = PieData(pieDataSet).apply { setValueFormatter(percentFormatter) }

        binding.genderChart.apply {
            data = pieData
            setUsePercentValues(true)
            description = null
        }
    }

    private fun updateAvailabilityChart() {
        bicyclesEntries.clear()
        val groupedBicycles = bicycleList.groupBy { it.inUse }
        val availableCount = groupedBicycles[false]?.count() ?: 0
        val inUseCount = groupedBicycles[true]?.count() ?: 0

        bicyclesEntries.add(PieEntry(inUseCount.toFloat(), "Em uso"))
        bicyclesEntries.add(PieEntry(availableCount.toFloat(), "Disponível"))
        binding.availableBikesChart.invalidate()
    }

    private fun setAvailablePieChart() {
        FirebaseHelper.getBicycles(communityId, false, object : RequestListener<Bike> {
            override fun onChildChanged(result: Bike) {
                bicycleList.find { it.id == result.id }.let { it?.inUse = result.inUse }
                updateAvailabilityChart()
            }

            override fun onChildAdded(result: Bike) {
                bicycleList.add(result)
                updateAvailabilityChart()
            }

            override fun onChildRemoved(result: Bike) {
                bicycleList.remove(result)
                updateAvailabilityChart()
            }

        })

        val availableDataSet = PieDataSet(bicyclesEntries, "Bicicletas disponiveis").apply {
            colors = pieChartColors
            valueTextSize = 14f
        }

        val data = PieData(availableDataSet).apply {
            setValueFormatter(percentFormatter)
        }

        binding.availableBikesChart.apply {
            this.data = data
            description = null
            setUsePercentValues(true)
            legend.isEnabled = false
            invalidate()
        }

    }


}
