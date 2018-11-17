package com.anoki.randomfakenamegenerator.ui.main

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.anoki.randomfakenamegenerator.R
import kotlinx.android.synthetic.main.main_fragment.*
import org.jetbrains.anko.selector
import org.jetbrains.anko.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

class SpacesItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
            outRect: Rect, view: View,
            parent: RecyclerView, state: RecyclerView.State
    ) {
        outRect.left = space
        outRect.right = space
        outRect.bottom = space
        outRect.top = 0
    }
}

class MainFragment : Fragment(), NumberPicker.OnValueChangeListener {

    private val DEFAULT_SPINNER_VALUE = "random"
    private val DEFAULT_AMOUNT = 1

    private var gender: String? = null
    private var region: String? = null
    private var amount: Int? = null
    private var oldAmount: Int? = null

    private var sharedPreferences: SharedPreferences? = null
    private val personViewModel: MainViewModel by viewModel()

    override fun onValueChange(p0: NumberPicker?, oldVal: Int, newVal: Int) {
        amount = newVal
    }

    override fun onResume() {
        super.onResume()

        sharedPreferences = activity?.getPreferences(Context.MODE_PRIVATE)

        sharedPreferences?.let {
            gender = it.getString("gender", DEFAULT_SPINNER_VALUE)
            region = it.getString("region", DEFAULT_SPINNER_VALUE)
            amount = it.getInt("amount", DEFAULT_AMOUNT)
            oldAmount = it.getInt("amount", DEFAULT_AMOUNT)

            gender_button.text = gender
            region_button.text = region
        }
        amount?.let {
            amount_text.text = it.toString()
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val regions = resources.getStringArray(R.array.regions).toList()
        val genders = resources.getStringArray(R.array.genders).toList()

        region_button.setOnClickListener {
            activity?.selector("Choose a region", regions) { _, i ->
                region = regions[i]
                sharedPreferences?.edit {
                    putString("region", region)
                }
                region_button.text = region
            }
        }

        gender_button.setOnClickListener {
            activity?.selector("Choose a gender", genders) { _, i ->
                gender = genders[i]
                sharedPreferences?.edit {
                    putString("gender", gender)
                }
                gender_button.text = gender
            }
        }

        var nbColumns = 1
        if (resources.configuration.orientation == ORIENTATION_LANDSCAPE) {
            nbColumns = 2
        }
        val llm = GridLayoutManager(activity, nbColumns)
        llm.orientation = RecyclerView.VERTICAL

        val adapter = PersonAdapter(this)

        personViewModel.persons.observe(this, Observer { pagedList ->
            if (pagedList.isEmpty()) {
                showEmptyView()
            } else {
                showEmptyView(false)
            }
            adapter.submitList(pagedList)
            progressBar.visibility = View.INVISIBLE
        })

        personViewModel.networkErrorEvent.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let {
                progressBar.visibility = View.INVISIBLE
                activity?.toast(it)
            }
        })

        recycler_view.addItemDecoration(SpacesItemDecoration(16))
        recycler_view.layoutManager = llm
        recycler_view.adapter = adapter

        generate_button.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            personViewModel.generate(
                    amount ?: DEFAULT_AMOUNT,
                    gender ?: DEFAULT_SPINNER_VALUE,
                    region ?: DEFAULT_SPINNER_VALUE
            )
        }

        amount_edit_button.setOnClickListener {
            context?.let {safeContext ->
                val numberPickerDialog =
                        MaterialDialog.Builder(safeContext).title(R.string.dialog_title)
                                .customView(R.layout.number_picker_dialog, false).positiveText(R.string.ok)
                                .negativeText(R.string.cancel).onPositive { dialog, _ ->
                                    oldAmount = amount
                                    dialog.dismiss()
                                    amount_text.text = amount.toString()
                                    sharedPreferences?.edit {
                                        putInt("amount", amount ?: DEFAULT_AMOUNT)
                                    }
                                }.onNegative { dialog, _ ->
                                    dialog.dismiss()
                                    amount = oldAmount
                                }
                                .build()
                val numberPicker =
                        numberPickerDialog.view.findViewById<android.widget.NumberPicker>(R.id.number_picker)
                numberPicker.maxValue = 20
                numberPicker.minValue = 1
                numberPicker.value = amount ?: DEFAULT_AMOUNT

                numberPicker.setOnValueChangedListener(this)

                numberPickerDialog.show()
            }

        }
    }

    private fun showEmptyView(show: Boolean = true) {
        if (show) {
            recycler_view.visibility = View.GONE
            empty_view.visibility = View.VISIBLE
        } else {
            recycler_view.visibility = View.VISIBLE
            empty_view.visibility = View.GONE
        }
    }
}
