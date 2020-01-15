import com.github.jk1.license.filter.*
import com.github.jk1.license.render.*

plugins {
  id("com.github.jk1.dependency-license-report") version "1.12"
}

licenseReport {
  configurations = arrayOf("releaseRuntimeClasspath")
  renderers = arrayOf<ReportRenderer>(SimpleHtmlReportRenderer(), JsonReportRenderer())
  filters = arrayOf<DependencyFilter>(LicenseBundleNormalizer())
}

tasks.register("clean", Delete::class) {
  delete(rootProject.buildDir)
}