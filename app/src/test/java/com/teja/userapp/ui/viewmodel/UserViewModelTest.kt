package com.teja.userapp.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.teja.userapp.data.model.Location
import com.teja.userapp.data.model.Name
import com.teja.userapp.data.model.Picture
import com.teja.userapp.data.model.Street
import com.teja.userapp.data.model.User
import com.teja.userapp.data.repository.UserRepository
import com.teja.userapp.ui.state.UserUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class UserViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var repository: UserRepository

    @Mock
    lateinit var observer: Observer<UserUiState>

    private lateinit var userViewModel: UserViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockitoAnnotations.openMocks(this)
        userViewModel = UserViewModel(repository)
        userViewModel.userUiState.observeForever(observer)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        userViewModel.userUiState.removeObserver(observer)
    }

    @Test
    fun `return success state when api is successful`() = runTest {
        val users = listOf(
            User(
                name = Name(first = "ABC", last = "EFG"), location = Location(
                    street = Street(
                        number = 1234, name = "Street"
                    )
                ), picture = Picture(
                    large = "large", medium = "thumbnail"
                )
            )
        )
        `when`(repository.fetchUsers(1)).thenReturn(
            users
        )

        userViewModel.getUsers(1)
        advanceUntilIdle()
        verify(observer).onChanged(UserUiState.Loading)
        verify(observer).onChanged(UserUiState.Success(users))
    }

    @Test
    fun `return no users available message when api gives empty list`() = runTest {
        `when`(repository.fetchUsers(1)).thenReturn(emptyList())
        userViewModel.getUsers(1)
        advanceUntilIdle()
        verify(observer).onChanged(UserUiState.Loading)
        verify(observer).onChanged(UserUiState.Error("No Users available"))
    }

    @Test
    fun `return error state when getUser api is unSuccessful`() = runTest {
        `when`(repository.fetchUsers(1)).thenThrow(
            RuntimeException()
        )

        userViewModel.getUsers(1)
        advanceUntilIdle()
        verify(observer).onChanged(UserUiState.Loading)
        verify(observer).onChanged(UserUiState.Error("Something went wrong."))
    }

    @Test
    fun `return loading state when api is called`() = runTest {
        userViewModel.getUsers(1)
        verify(observer).onChanged(UserUiState.Loading)
    }
}
