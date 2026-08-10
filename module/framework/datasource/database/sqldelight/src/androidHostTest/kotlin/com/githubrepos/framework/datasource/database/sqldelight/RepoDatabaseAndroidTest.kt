package com.githubrepos.framework.datasource.database.sqldelight

import androidx.test.core.app.ApplicationProvider
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
internal class RepoDatabaseAndroidTest :
    RepoDatabaseTest(DatabaseDriverFactoryImp(ApplicationProvider.getApplicationContext(), TEST_REPO_DATABASE_NAME))