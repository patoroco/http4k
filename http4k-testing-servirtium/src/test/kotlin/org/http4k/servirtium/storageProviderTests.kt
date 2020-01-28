package org.http4k.servirtium

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.throws
import org.http4k.cloudnative.Unauthorized
import org.http4k.core.Credentials
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.Uri
import org.http4k.testing.ApprovalTest
import org.http4k.testing.Approver
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.nio.file.Path

@ExtendWith(ApprovalTest::class)
class GitHubStorageProviderTest {

    @Test
    fun `can load content from github`(approver: Approver) {
        val storage = gitHubFor(FakeGitHub.credentials)
        approver.assertApproved(Response(OK).body(String(storage("gitHubFile.json").get())))
    }

    @Test
    fun `wrong credentials fails`() {
        val storage = gitHubFor(Credentials("notuser", "noway"))
        assertThat({ storage("gitHubFile.json").get() }, throws<Unauthorized>())
    }

    private fun gitHubFor(credentials: Credentials) = InteractionStorage.Github(
        "owner",
        "repo",
        credentials,
        Path.of("org/http4k/servirtium"),
        "master",
        Uri.of("https://api.github.com"),
        FakeGitHub()
    )
}
