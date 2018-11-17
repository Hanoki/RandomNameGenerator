package com.anoki.randomfakenamegenerator.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.anoki.randomfakenamegenerator.R
import com.anoki.randomfakenamegenerator.model.Person
import com.bumptech.glide.Glide
import com.google.gson.Gson
import kotlinx.android.synthetic.main.details_fragment.*

fun String.firstLetterCaps(): String = this.first().toUpperCase() + this.substring(1)

class DetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.details_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = DetailsFragmentArgs.fromBundle(arguments)
        val person = Gson().fromJson(args.personJson, Person::class.java)
        details_person_name.text = person.name
        details_person_surname.text = person.surname
        details_person_age.text = person.age.toString()
        details_person_region.text = person.region
        details_person_birthday.text = person.birthday.mdy
        details_person_gender.text = person.gender.firstLetterCaps()
        Glide.with(this).load(person.photo).into(details_person_photo)
    }
}