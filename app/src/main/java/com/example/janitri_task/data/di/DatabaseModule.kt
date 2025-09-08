package com.example.janitri_task.data.di

import android.content.Context
import androidx.room.Room
import com.example.janitri_task.data.local.AppDatabase
import com.example.janitri_task.data.local.VitalRecordDao
import com.example.janitri_task.data.repository.VitalRepositoryImpl
import com.example.janitri_task.domain.repository.VitalRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "vitals_db"
        ).build()
    }

    @Provides
    fun provideVitalDao(db: AppDatabase): VitalRecordDao = db.vitalRecordDao()

    @Provides
    @Singleton
    fun provideVitalRepository(dao: VitalRecordDao): VitalRepository {
        return VitalRepositoryImpl(dao)
    }

}
