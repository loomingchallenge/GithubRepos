Sample program written using Kotlin(Compose) Multiplatform and inspired by clean architecture.  
It can be used as a template for other Kotlin Multiplatform projects.   
Runs on Android, iOS, JVM.  

The software downloads and displays a list of repositories. 
Supports paging, in-memory and database caching using cash.paging, Store5, SQLDelight, Ktor, Multiplatform-Settings. 
The code is divided into modules that are in the core, adapter, and framework layers. 
Modules in higher layers are independent of frameworks, so each framework module can be easily replaced.  
  
<pre>
Configurations:  
androidApp  
  Module: GithubRepos.composeApp.main  
iosApp  
gradle jvmFull  
  Run: composeApp:jvmRun -DmainClass=MainKt --quiet  
  Environment variables: FLAVOR=Full; DEBUG=true  
gradle jvmDemo  
  Run: composeApp:jvmRun -DmainClass=MainKt --quiet  
  Environment variables: FLAVOR=Demo; DEBUG=true  
gradle web  
  Run: :composeApp:wasmJsRun  
</pre>