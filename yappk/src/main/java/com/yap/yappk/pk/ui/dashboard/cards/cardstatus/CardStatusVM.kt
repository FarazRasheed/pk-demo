package com.yap.yappk.pk.ui.dashboard.cards.cardstatus

import com.digitify.core.base.BaseViewModel
import com.yap.yappk.localization.*
import com.yap.yappk.pk.di.ResourcesProviders
import com.yap.yappk.pk.ui.dashboard.cards.cardstatus.adapter.StatusDataModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CardStatusVM @Inject constructor(
    override val viewState: CardStatusState,
    private val resourcesProvider: ResourcesProviders
) : BaseViewModel<ICardStatus.State>(), ICardStatus.ViewModel {
    override fun handleStatus(): ArrayList<StatusDataModel> {
        val list = ArrayList<StatusDataModel>()
        list.add(
            StatusDataModel(
                statusTitle = resourcesProvider.getString(
                    screen_card_status_display_text_status_ordering
                )
            )
        )
        list.add(
            StatusDataModel(
                statusTitle = resourcesProvider.getString(
                    screen_card_status_display_text_status_building
                )
            )
        )
        list.add(
            StatusDataModel(
                statusTitle = resourcesProvider.getString(
                    screen_card_status_display_text_status_shipping
                )
            )
        )
        return list
    }

    override fun handleShippedStatus(): ArrayList<StatusDataModel> {
        val list = ArrayList<StatusDataModel>()

        list.add(
            StatusDataModel(
                statusTitle = resourcesProvider.getString(
                    screen_card_status_display_text_status_ordered
                ), markerInProgress = false, lineInProgress = false
            )
        )
        list.add(
            StatusDataModel(
                statusTitle = resourcesProvider.getString(
                    screen_card_status_display_text_status_built
                ), markerInProgress = false, lineInProgress = false
            )
        )
        list.add(
            StatusDataModel(
                statusTitle = resourcesProvider.getString(
                    screen_card_status_display_text_status_shipped
                ), markerInProgress = false
            )
        )
        return list
    }

    override fun handleShippingStatus(): ArrayList<StatusDataModel> {
        val list = ArrayList<StatusDataModel>()
        list.add(
            StatusDataModel(
                statusTitle = resourcesProvider.getString(
                    screen_card_status_display_text_status_ordered
                ), markerInProgress = false, lineInProgress = false
            )
        )
        list.add(
            StatusDataModel(
                statusTitle = resourcesProvider.getString(
                    screen_card_status_display_text_status_built
                ), markerInProgress = false, lineInProgress = false
            )
        )
        list.add(
            StatusDataModel(
                statusTitle = resourcesProvider.getString(
                    screen_card_status_display_text_status_shipping
                )
            )
        )
        return list
    }

    override fun handleBookedStatus(): ArrayList<StatusDataModel> {
        val list = ArrayList<StatusDataModel>()
        list.add(
            StatusDataModel(
                statusTitle = resourcesProvider.getString(
                    screen_card_status_display_text_status_ordered
                ), markerInProgress = false, lineInProgress = false
            )
        )
        list.add(
            StatusDataModel(
                statusTitle = resourcesProvider.getString(
                    screen_card_status_display_text_status_building
                ), markerInProgress = false
            )
        )
        list.add(
            StatusDataModel(
                statusTitle = resourcesProvider.getString(
                    screen_card_status_display_text_status_shipping
                )
            )
        )
        return list
    }

    override fun handleOrderedStatus(): ArrayList<StatusDataModel> {
        val list = ArrayList<StatusDataModel>()
        list.add(
            StatusDataModel(
                statusTitle = resourcesProvider.getString(
                    screen_card_status_display_text_status_ordered
                ), markerInProgress = false
            )
        )
        list.add(
            StatusDataModel(
                statusTitle = resourcesProvider.getString(
                    screen_card_status_display_text_status_building
                )
            )
        )
        list.add(
            StatusDataModel(
                statusTitle = resourcesProvider.getString(
                    screen_card_status_display_text_status_shipping
                )
            )
        )
        return list
    }

}