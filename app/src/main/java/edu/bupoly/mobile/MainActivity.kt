package edu.bupoly.mobile

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Message
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.card.MaterialCardView

class MainActivity : AppCompatActivity() {
    private lateinit var web: WebView
    private lateinit var swipe: SwipeRefreshLayout
    private lateinit var toolbar: MaterialToolbar
    private lateinit var container: ViewGroup
    private var page = "home"

    private val srms = "https://bupoly.safsrms.com/"
    private val website = "https://bupoly.edu.ng/"
    private val appVersion = "3.0.0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        container = findViewById(R.id.container)
        swipe = findViewById(R.id.swipeRefresh)

        setupNavigation()
        setupBackNavigation()
        showHome()
    }

    private fun setupNavigation() {
        findViewById<BottomNavigationView>(R.id.bottomNav).setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> { showHome(); true }
                R.id.nav_student -> { showStudent(); true }
                R.id.nav_info -> { showInfo(); true }
                R.id.nav_more -> { showMore(); true }
                else -> false
            }
        }
        swipe.setOnRefreshListener {
            if (page == "srms" || page == "web") web.reload()
            else { showHome(); swipe.isRefreshing = false }
        }
    }

    private fun setupBackNavigation() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                when {
                    (page == "srms" || page == "web") && web.canGoBack() -> web.goBack()
                    page != "home" -> showHome()
                    else -> finish()
                }
            }
        })
    }

    private fun clear(title: String) {
        page = title.lowercase().replace(" ", "_")
        toolbar.title = title
        container.removeAllViews()
    }

    private fun rootLayout(): LinearLayout = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        setPadding(20, 20, 20, 20)
        layoutParams = ViewGroup.LayoutParams(-1, -1)
    }

    private fun text(text: String, size: Float = 16f): TextView = TextView(this).apply {
        this.text = text
        textSize = size
        setTextColor(ContextCompat.getColor(this@MainActivity, R.color.text_primary))
        setPadding(4, 8, 4, 8)
    }

    private fun subtitle(text: String): TextView = TextView(this).apply {
        this.text = text
        textSize = 14f
        setTextColor(ContextCompat.getColor(this@MainActivity, R.color.text_secondary))
        setPadding(4, 0, 4, 12)
    }

    private fun actionButton(label: String, action: () -> Unit) =
        com.google.android.material.button.MaterialButton(this).apply {
            this.text = label
            isAllCaps = false
            setOnClickListener { action() }
            layoutParams = LinearLayout.LayoutParams(-1, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                bottomMargin = 10
            }
        }

    private fun featureCard(title: String, description: String, action: () -> Unit): View {
        val card = MaterialCardView(this).apply {
            radius = 18f
            cardElevation = 2f
            strokeWidth = 1
            strokeColor = ContextCompat.getColor(this@MainActivity, R.color.card_stroke)
            setOnClickListener { action() }
            layoutParams = LinearLayout.LayoutParams(-1, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                bottomMargin = 12
            }
        }
        val box = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(22, 18, 22, 18)
        }
        box.addView(text(title, 18f).apply { setTypeface(typeface, android.graphics.Typeface.BOLD) })
        box.addView(subtitle(description))
        card.addView(box)
        return card
    }

    private fun sectionHeader(label: String) = text(label, 20f).apply {
        setTypeface(typeface, android.graphics.Typeface.BOLD)
        setPadding(4, 12, 4, 10)
    }

    private fun showHome() {
        clear("BUPOLY Mobile")
        val root = rootLayout()

        val hero = MaterialCardView(this).apply {
            radius = 22f
            setCardBackgroundColor(ContextCompat.getColor(this@MainActivity, R.color.bupoly_green))
            layoutParams = LinearLayout.LayoutParams(-1, ViewGroup.LayoutParams.WRAP_CONTENT).apply { bottomMargin = 16 }
        }
        val heroBox = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(26, 28, 26, 28)
        }
        heroBox.addView(text("BINYAMINU USMAN POLYTECHNIC", 23f).apply {
            setTextColor(android.graphics.Color.WHITE)
            setTypeface(typeface, android.graphics.Typeface.BOLD)
        })
        heroBox.addView(text("Hadejia, Jigawa State", 15f).apply { setTextColor(0xFFD7EDE3.toInt()) })
        heroBox.addView(text("Official mobile portal", 14f).apply { setTextColor(0xFFE6F3EE.toInt()) })
        heroBox.addView(text("One app for student services, institutional information and future digital services.", 13f).apply {
            setTextColor(0xFFE6F3EE.toInt())
            setPadding(4, 12, 4, 0)
        })
        hero.addView(heroBox)
        root.addView(hero)

        root.addView(sectionHeader("Student Services"))
        root.addView(featureCard("Student SRMS", "Registration, student records and authenticated academic services.") { openSrms() })
        root.addView(featureCard("Student Results", "Open the student portal for result-related services.") { openSrms() })
        root.addView(featureCard("Fees & Payments", "Access available fee and payment services through the official systems.") { openSrms() })
        root.addView(featureCard("Student ID", "Student identity services can be connected here when the backend endpoint is available.") { openSrms() })

        root.addView(sectionHeader("BUPOLY Information"))
        root.addView(featureCard("News & Announcements", "View official institutional updates and notices.") { openOfficial("News & Announcements") })
        root.addView(featureCard("Admissions", "Access current admission information from the official Polytechnic website.") { openOfficial("Admissions") })
        root.addView(featureCard("Academic Departments", "Explore academic departments and institutional information.") { openOfficial("Academic Departments") })
        root.addView(featureCard("Campus Directory", "Open official institutional information and directory resources.") { openOfficial("Campus Directory") })

        root.addView(sectionHeader("Support"))
        root.addView(featureCard("Help & Contact", "Open the official BUPOLY website for current contact and support information.") { openOfficial("Help & Contact") })
        root.addView(featureCard("Notifications", "Notification centre prepared for future Firebase push notifications.") { showNotifications() })

        container.addView(root)
    }

    private fun showStudent() {
        clear("Student Services")
        val root = rootLayout()
        root.addView(text("Student Portal", 24f).apply { setTypeface(typeface, android.graphics.Typeface.BOLD) })
        root.addView(subtitle("Access authenticated student services through the existing BUPOLY SRMS."))
        root.addView(featureCard("Student SRMS", "Open the official student records system.") { openSrms() })
        root.addView(featureCard("Results", "Use the SRMS for student result services.") { openSrms() })
        root.addView(featureCard("Fees & Payments", "Use the connected student system for fee-related services.") { openSrms() })
        root.addView(featureCard("Student ID", "Student ID functions can be enabled here once the official endpoint is provided.") { openSrms() })
        root.addView(featureCard("Registration", "Continue to the SRMS for student registration services.") { openSrms() })
        container.addView(root)
    }

    private fun showInfo() {
        clear("BUPOLY Information")
        val root = rootLayout()
        root.addView(text("Institutional Information", 24f).apply { setTypeface(typeface, android.graphics.Typeface.BOLD) })
        root.addView(subtitle("Public information is kept linked to the official BUPOLY website so that updates remain current."))
        root.addView(featureCard("News & Announcements", "Official public notices and institutional updates.") { openOfficial("News & Announcements") })
        root.addView(featureCard("Admissions", "Admission information and instructions.") { openOfficial("Admissions") })
        root.addView(featureCard("Academic Departments", "Schools, departments and academic information.") { openOfficial("Academic Departments") })
        root.addView(featureCard("Campus Directory", "Institutional and directory resources.") { openOfficial("Campus Directory") })
        root.addView(featureCard("Official Website", website) { openUrlInBrowser(website) })
        root.addView(text("Binyaminu Usman Polytechnic", 19f).apply { setTypeface(typeface, android.graphics.Typeface.BOLD) })
        root.addView(subtitle("P.M.B 013, Hadejia, Jigawa State"))
        container.addView(root)
    }

    private fun showMore() {
        clear("More")
        val root = rootLayout()
        root.addView(text("BUPOLY Mobile", 24f).apply { setTypeface(typeface, android.graphics.Typeface.BOLD) })
        root.addView(subtitle("Version $appVersion"))
        root.addView(featureCard("Notifications", "Notification centre and future push-notification support.") { showNotifications() })
        root.addView(featureCard("Help & Contact", "Official help and current contact information.") { openOfficial("Help & Contact") })
        root.addView(featureCard("Open Website in Browser", "Open bupoly.edu.ng outside the app.") { openUrlInBrowser(website) })
        root.addView(actionButton("Refresh App") { showHome() })
        root.addView(text("Production roadmap", 20f).apply { setTypeface(typeface, android.graphics.Typeface.BOLD); setPadding(4, 16, 4, 8) })
        root.addView(subtitle("Native public portal modules are available now. SRMS remains connected to the existing backend. Firebase notifications, a dedicated e-payment gateway, student ID API and campus directory API can be connected without redesigning the app."))
        container.addView(root)
    }

    private fun showNotifications() {
        clear("Notifications")
        val root = rootLayout()
        root.addView(text("Notification Centre", 24f).apply { setTypeface(typeface, android.graphics.Typeface.BOLD) })
        root.addView(subtitle("This screen is ready for official BUPOLY push notifications."))
        val empty = MaterialCardView(this).apply {
            radius = 18f
            cardElevation = 1f
            layoutParams = LinearLayout.LayoutParams(-1, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
        val content = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; gravity = Gravity.CENTER; setPadding(24, 36, 24, 36) }
        content.addView(text("No new notifications", 18f).apply { gravity = Gravity.CENTER })
        content.addView(subtitle("When Firebase Cloud Messaging is configured, admissions notices, registration deadlines, results announcements and institutional alerts can appear here."))
        empty.addView(content)
        root.addView(empty)
        container.addView(root)
    }

    private fun openOfficial(label: String) {
        openWebPage(website, label)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun openSrms() {
        openWebPage(srms, "Student SRMS")
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun openWebPage(url: String, title: String) {
        page = if (url == srms) "srms" else "web"
        container.removeAllViews()
        toolbar.title = title
        web = WebView(this).apply {
            layoutParams = ViewGroup.LayoutParams(-1, -1)
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.allowFileAccess = false
            settings.allowContentAccess = true
            settings.setSupportMultipleWindows(false)
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW
            settings.javaScriptCanOpenWindowsAutomatically = false
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                    val target = request.url.toString()
                    val host = request.url.host.orEmpty()
                    return if (host.endsWith("bupoly.safsrms.com") || host.endsWith("bupoly.edu.ng")) {
                        false
                    } else {
                        openUrlInBrowser(target)
                        true
                    }
                }
                override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) { swipe.isRefreshing = true }
                override fun onPageFinished(view: WebView, url: String) { swipe.isRefreshing = false }
            }
            webChromeClient = object : WebChromeClient() {}
            setDownloadListener { downloadUrl, _, _, _, _ -> openUrlInBrowser(downloadUrl) }
        }
        container.addView(web)
        web.loadUrl(url)
    }

    private fun openUrlInBrowser(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }
}
