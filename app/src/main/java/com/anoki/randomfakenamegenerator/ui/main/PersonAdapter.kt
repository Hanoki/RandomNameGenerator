package com.anoki.randomfakenamegenerator.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.anoki.randomfakenamegenerator.R
import com.anoki.randomfakenamegenerator.model.Person
import com.bumptech.glide.Glide
import com.google.gson.Gson
import de.hdodenhof.circleimageview.CircleImageView

class PersonAdapter(val fragment: Fragment) :
        PagedListAdapter<Person, PersonAdapter.PersonViewHolder>(DIFF_CALLBACK) {

    private lateinit var recyclerView: RecyclerView

    /**
     * Called by RecyclerView when it starts observing this Adapter.
     *
     *
     * Keep in mind that same adapter may be observed by multiple RecyclerViews.
     *
     * @param recyclerView The RecyclerView instance which started observing this adapter.
     * @see .onDetachedFromRecyclerView
     */
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val itemView =
                LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
        return PersonViewHolder(fragment, itemView)
    }

    class PersonViewHolder(private val fragment: Fragment, itemView: View) :
            RecyclerView.ViewHolder(itemView) {

        private var nameTextView: TextView = itemView.findViewById(R.id.item_person_name)
        private var surnameTextView: TextView = itemView.findViewById(R.id.item_person_surname)
        private var photoImageView: CircleImageView = itemView.findViewById(R.id.item_person_photo)
        private var ageTextView: TextView = itemView.findViewById(R.id.item_person_age)
        private var regionTextView: TextView = itemView.findViewById(R.id.item_person_region)

        fun bindTo(person: Person) {
            nameTextView.text = person.name
            surnameTextView.text = person.surname
            Glide.with(fragment).load(person.photo).into(photoImageView)
            ageTextView.text = person.age.toString()
            regionTextView.text = person.region

            fragment.activity?.resources?.let {
                itemView.findViewById<CardView>(R.id.item_cardview).setCardBackgroundColor(it.getColor(if (person.gender == "male") {
                    R.color.material_blue
                } else {
                    R.color.material_pink
                }))
            }

            itemView.setOnClickListener {
                val personJson = Gson().toJson(person)
                val action = MainFragmentDirections.actionMainFragmentToDetailsFragment(
                        personJson
                )
                action.setPersonJson(personJson)
                itemView.findNavController().navigate(action)
            }
        }

        fun clear() {
            nameTextView.text = ""
        }
    }

    /**
     * Called when the current PagedList is updated.
     *
     *
     * This may be dispatched as part of [.submitList] if a background diff isn't
     * needed (such as when the first list is passed, or the list is cleared). In either case,
     * PagedListAdapter will simply call
     * [notifyItemRangeInserted/Removed(0, mPreviousSize)][.notifyItemRangeInserted].
     *
     *
     * This method will *not*be called when the Adapter switches from presenting a PagedList
     * to a snapshot version of the PagedList during a diff. This means you cannot observe each
     * PagedList via this method.
     *
     * @param currentList new PagedList being displayed, may be null.
     */
    override fun onCurrentListChanged(currentList: PagedList<Person>?) {
        super.onCurrentListChanged(currentList)

        //Scroll to the top of the list when new items are added
        recyclerView.scrollToPosition(0)
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        val person = getItem(position)
        if (person != null) {
            holder.bindTo(person)
        } else {
            // Null defines a placeholder item - PagedListAdapter automatically
            // invalidates this row when the actual object is loaded from the
            // database.
            holder.clear()
        }
    }

    companion object {
        private val DIFF_CALLBACK = object :
                DiffUtil.ItemCallback<Person>() {
            // Concert details may have changed if reloaded from the database,
            // but ID is fixed.
            override fun areItemsTheSame(
                    oldPerson: Person,
                    newPerson: Person
            ): Boolean =
                    oldPerson.id == newPerson.id

            override fun areContentsTheSame(
                    oldPerson: Person,
                    newPerson: Person
            ): Boolean =
                    oldPerson == newPerson
        }
    }
}