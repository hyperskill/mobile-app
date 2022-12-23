package org.hyperskill.app.items.injection

import org.hyperskill.app.items.domain.interactor.ItemsInteractor

interface ItemsDataComponent {
    val itemsInteractor: ItemsInteractor
}