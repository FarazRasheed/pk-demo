package com.yap.yappk.pk.ui.kyc.address.city.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.digitify.core.base.BaseBindingSearchRecyclerAdapter
import com.yap.yappk.R
import com.yap.yappk.databinding.LayoutItemCityNameBinding
import com.yap.yappk.networking.microservices.customers.responsedtos.City
import java.util.*

class CitiesAdapter(
    private val list: MutableList<City>
) : BaseBindingSearchRecyclerAdapter<City, CitiesAdapter.CitiesItemViewHolder>(list) {

    override fun getLayoutIdForViewType(viewType: Int): Int = R.layout.layout_item_city_name

    override fun onCreateViewHolder(binding: ViewDataBinding): CitiesAdapter.CitiesItemViewHolder {
        return CitiesItemViewHolder(binding as LayoutItemCityNameBinding)
    }

    override fun onBindViewHolder(holder: CitiesItemViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.onBind(list[position])
    }

    override fun filterItem(constraint: CharSequence?, item: City): Boolean {
        val filterString = constraint.toString().lowercase(Locale.ROOT)
        val cityName = item.name?.lowercase(Locale.ROOT) ?: ""

        return cityName.contains(filterString)
    }

    inner class CitiesItemViewHolder(
        private val itemBinding: LayoutItemCityNameBinding
    ) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun onBind(city: City) {
            itemBinding.tvCityName.text = city.name
        }
    }
}