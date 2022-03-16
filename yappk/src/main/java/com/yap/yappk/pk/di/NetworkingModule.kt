package com.yap.yappk.pk.di

import android.content.Context
import com.digitify.core.utils.SharedPreferenceManager
import com.yap.yappk.networking.apiclient.base.RetroNetwork
import com.yap.yappk.networking.microservices.authentication.AuthRetroService
import com.yap.yappk.networking.microservices.cards.CardsRetroService
import com.yap.yappk.networking.microservices.customers.CustomersRetroService
import com.yap.yappk.networking.microservices.messages.MessagesRetroService
import com.yap.yappk.networking.microservices.transactions.TransactionsRetroService
import com.yap.yappk.pk.configurations.AppConfigurations
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetworkingModule {
    @Provides
    @Singleton
    fun provideRetroNetwork(): RetroNetwork {
        return AppConfigurations.getRetrofit()!!
    }

    @Singleton
    @Provides
    fun provideAuthService(retroNetwork: RetroNetwork): AuthRetroService {
        return retroNetwork.createService(AuthRetroService::class.java)
    }

    @Singleton
    @Provides
    fun provideMessagesService(retroNetwork: RetroNetwork): MessagesRetroService {
        return retroNetwork.createService(MessagesRetroService::class.java)
    }

    @Singleton
    @Provides
    fun provideCustomersService(retroNetwork: RetroNetwork): CustomersRetroService {
        return retroNetwork.createService(CustomersRetroService::class.java)
    }

    @Singleton
    @Provides
    fun provideTransactionService(retroNetwork: RetroNetwork): TransactionsRetroService {
        return retroNetwork.createService(TransactionsRetroService::class.java)
    }

    @Singleton
    @Provides
    fun provideRetroService(retroNetwork: RetroNetwork): CardsRetroService {
        return retroNetwork.createService(CardsRetroService::class.java)
    }

    @Singleton
    @Provides
    fun provideSharedPreferenceManager(@ApplicationContext context: Context) =
        SharedPreferenceManager(context)

}