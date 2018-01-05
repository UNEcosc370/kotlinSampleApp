package au.edu.une.grewsum.grewsum

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.app.Fragment
import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_grazing_period.*
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import kotlinx.android.synthetic.main.fragment_feed_budget.*
import java.text.DateFormat
import java.text.ParseException
import android.widget.DatePicker
import android.app.DatePickerDialog
import java.util.*


class MainActivity : Activity() {

    class PaddocksFragment : Fragment() {
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup,
                                  savedInstanceState: Bundle?): View {
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_paddocks, container, false)
        }
    }

    class FeedBudgetFragment : Fragment() {

        fun dateToYMD(d:Date):Triple<Int,Int,Int> {
            val c = Calendar.getInstance()
            c.time = d
            return Triple(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH))
        }

        val fb = FeedBudget(null, null, null, null, null, null, null)

        val dateFormat = DateFormat.getDateInstance()

        fun updateData() {
            fb.farmArea = fbfarmArea.text.toString().toDoubleOrNull()
            fb.growthRate = fbGrowthRate.text.toString().toDoubleOrNull()
            fb.start = try { dateFormat.parse(fbStartDate.text.toString()) } catch (e:ParseException) { null }
            fb.end = try { dateFormat.parse(fbEndDate.text.toString()) } catch (e:ParseException) { null }
            fb.startHerbageMass = fbStartMass.text.toString().toDoubleOrNull()
            fb.endHerbageMass = fbEndMass.text.toString().toDoubleOrNull()
            fb.dsePerHead = fbDsePerHead.text.toString().toDoubleOrNull()

            val af = fb.availableFeed()
            if (af != null) {
                fbAvailableFeed.text = "Available feed is ${"%.2f".format(af)} kg DM / ha"
            } else {
                fbAvailableFeed.text = "Available feed"
            }

            val sd = fb.stockDensity()
            if (sd != null) {
                fbStockDensity.text = "Stock density can be ${"%.2f".format(sd)} per ha"
            } else {
                fbStockDensity.text = "Stock density"
            }

            val su = fb.stockUnits()
            if (sd != null) {
                fbStockUnits.text = "Up to ${"%.2f".format(su)} stock units total"
            } else {
                fbStockUnits.text = "Stock units"
            }


        }

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup,
                         savedInstanceState: Bundle?): View {
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_feed_budget, container, false)
        }

        override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            val watcher = object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

                override fun afterTextChanged(p0: Editable?) {
                    updateData()
                }
            }


            fbfarmArea.addTextChangedListener(watcher)
            fbGrowthRate.addTextChangedListener(watcher)
            fbStartDate.addTextChangedListener(watcher)
            fbEndDate.addTextChangedListener(watcher)
            fbStartMass.addTextChangedListener(watcher)
            fbEndMass.addTextChangedListener(watcher)
            fbDsePerHead.addTextChangedListener(watcher)


            fbStartDate.setOnFocusChangeListener { view, hasFocus ->

                val (y, m, d) = dateToYMD(fb.start ?: Date())

                if(hasFocus) {
                    val dpd = DatePickerDialog(activity, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        val c = Calendar.getInstance()
                        c.set(year, monthOfYear, dayOfMonth)
                        val d = c.time
                        val s = dateFormat.format(d)
                        fb.start = d
                        fbStartDate.setText(s)
                        updateData()
                    }, y, m, d)

                    dpd.show()
                }
            }

            fbEndDate.setOnFocusChangeListener { view, hasFocus ->

                val (y, m, d) = dateToYMD(fb.end ?: Date())

                if(hasFocus) {
                    val dpd = DatePickerDialog(activity, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        val c = Calendar.getInstance()
                        c.set(year, monthOfYear, dayOfMonth)
                        val d = c.time
                        val s = dateFormat.format(d)
                        fb.end = d
                        fbEndDate.setText(s)
                        updateData()
                    }, y, m, d)

                    dpd.show()
                }
            }

        }


    }

    class GrazingPeriodFragment : Fragment() {

        val gp = GrazingPeriod(null, null, null, null, null)

        fun updateData() {
            gp.paddockArea = gpPaddockArea.text.toString().toDoubleOrNull()
            gp.startMass = gpStartMass.text.toString().toDoubleOrNull()
            gp.residualMass = gpEndMass.text.toString().toDoubleOrNull()
            gp.dsePerHead = gpDsePerHead.text.toString().toDoubleOrNull()
            gp.stockUnits = gpStockUnits.text.toString().toDoubleOrNull()

            val period = gp.grazingPeriod()
            if (period != null) {
                gpGrazingPeriod.text = "Grazing period ${"%.2f".format(period)} days"
            } else {
                gpGrazingPeriod.text = "Grazing period"
            }
        }

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup,
                                  savedInstanceState: Bundle?): View {
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_grazing_period, container, false)
        }


        override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            val watcher = object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

                override fun afterTextChanged(p0: Editable?) {
                    updateData()
                }
            }

            gpPaddockArea.addTextChangedListener(watcher)
            gpStartMass.addTextChangedListener(watcher)
            gpEndMass.addTextChangedListener(watcher)
            gpDsePerHead.addTextChangedListener(watcher)
            gpStockUnits.addTextChangedListener(watcher)
        }
    }

    private fun loadFeedBudget() {
        val fragmentManager = fragmentManager
        val feedBudgetFrag = FeedBudgetFragment()
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(subcontents.id, feedBudgetFrag)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    private fun loadGrazingPeriod() {
        val fragmentManager = fragmentManager
        val grazingPeriodFrag = GrazingPeriodFragment()
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(subcontents.id, grazingPeriodFrag)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    private fun loadPaddocks() {
        val fragmentManager = fragmentManager
        val paddocksFrag = PaddocksFragment()
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(subcontents.id, paddocksFrag)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_paddocks -> {
                loadPaddocks()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_feed_budget -> {
                loadFeedBudget()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_grazing_period -> {
                loadGrazingPeriod()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigation.selectedItemId = R.id.navigation_feed_budget
    }
}
