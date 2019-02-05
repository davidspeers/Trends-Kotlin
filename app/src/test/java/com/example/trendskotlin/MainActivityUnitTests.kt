import android.content.Context
import com.example.trendskotlin.MainActivity
import com.example.trendskotlin.R
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.runners.MockitoJUnitRunner

private const val FAKE_STRING = "HELLO WORLD"

@RunWith(MockitoJUnitRunner::class)
class UnitTestSample {

    @Mock
    private lateinit var mockContext: Context

    @Test
    fun readStringFromContext_LocalizedString() {
        // Given a mocked Context injected into the object under test...
        `when`(mockContext.getString(R.string.numPlayers))
            .thenReturn(FAKE_STRING)
        //val myObjectUnderTest = MainActivity(mockContext)

        // ...when the string is returned from the object under test...
        //val result: String = myObjectUnderTest.getHelloWorldString()

        // ...then the result should be the expected one.
        //assertThat(result, `is`(FAKE_STRING))
    }
}