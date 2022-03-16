package com.yap.yappk.pk.di

import com.yap.yappk.networking.microservices.authentication.AuthApi
import com.yap.yappk.networking.microservices.authentication.AuthRepository
import com.yap.yappk.networking.microservices.cards.CardsApi
import com.yap.yappk.networking.microservices.cards.CardsRepository
import com.yap.yappk.networking.microservices.customers.CustomersApi
import com.yap.yappk.networking.microservices.customers.CustomersRepository
import com.yap.yappk.networking.microservices.messages.MessagesApi
import com.yap.yappk.networking.microservices.messages.MessagesRepository
import com.yap.yappk.networking.microservices.transactions.TransactionsApi
import com.yap.yappk.networking.microservices.transactions.TransactionsRepository
import com.yap.yappk.pk.utils.NameValidator
import com.yap.yappk.pk.utils.PersonNameValidator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    @Singleton
    abstract fun provideAuthRepository(authRepository: AuthRepository): AuthApi

    @Binds
    @Singleton
    abstract fun provideMessagesRepository(messagesRepository: MessagesRepository): MessagesApi

    @Binds
    @Singleton
    abstract fun provideCustomersRepository(customersRepository: CustomersRepository): CustomersApi

    @Binds
    @Singleton
    abstract fun provideCardsRepository(cardsRepository: CardsRepository): CardsApi

    @Binds
    @Singleton
    abstract fun provideTransactionsRepository(transactionsRepository: TransactionsRepository): TransactionsApi

    @Binds
    @Singleton
    abstract fun provideNameValidator(personNameValidator: PersonNameValidator): NameValidator


}