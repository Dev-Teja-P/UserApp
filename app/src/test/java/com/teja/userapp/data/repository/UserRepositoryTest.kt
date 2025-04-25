package com.teja.userapp.data.repository

import com.teja.userapp.data.model.Location
import com.teja.userapp.data.model.Name
import com.teja.userapp.data.model.Picture
import com.teja.userapp.data.model.Street
import com.teja.userapp.data.model.User
import com.teja.userapp.data.model.UserResponse
import com.teja.userapp.data.remote.UserApi
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class UserRepositoryTest {
    @Mock
    lateinit var userApi: UserApi

    private lateinit var userRepository: UserRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        userRepository = UserRepository(userApi)
    }

    @Test
    fun `return users list when api is successful`() = runTest {
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
        `when`(userApi.getUsers(1)).thenReturn(UserResponse(users))
        val result = userRepository.fetchUsers(1)

        assertEquals(users, result)
        assertEquals(1, result.size)
    }

    @Test(expected = Exception::class)
    fun `return error when api is unSuccessful`() = runTest {
        `when`(userApi.getUsers(1)).thenThrow(Exception())
        userRepository.fetchUsers(1)
    }
}
