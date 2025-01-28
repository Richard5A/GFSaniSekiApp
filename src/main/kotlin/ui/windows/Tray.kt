package ui.windows

import Main
import java.awt.*
import java.awt.TrayIcon.MessageType
import java.net.URL
import javax.swing.JOptionPane
import kotlin.system.exitProcess

val tray: SystemTray by lazy {
    SystemTray.getSystemTray()
}
private lateinit var trayIcon: TrayIcon

const val TRAY_ICON_TOOLTIP = "Einsatzmelder"

fun getTrayIcon(): TrayIcon {
    if (!::trayIcon.isInitialized) {
        val url: URL? = Main::class.java.getResource("logo.png")
        val image = Toolkit.getDefaultToolkit().getImage(url)
        val popup = PopupMenu()
        popup.add(MenuItem("Anwendung schließen").also { it.addActionListener { exitProcess(0) } })
        trayIcon = TrayIcon(image, TRAY_ICON_TOOLTIP, popup)
        trayIcon.isImageAutoSize = true
        tray.add(trayIcon)
    }
    return trayIcon
}

fun notify(message: String, messageType: MessageType = MessageType.INFO) {
    if (SystemTray.isSupported()) {
        trayIcon.displayMessage("Einsatzmelder", message, messageType)
    } else {
        // Fallback for systems that don't support SystemTray
        JOptionPane.showMessageDialog(
            null,
            message,
            "Einsatzmelder",
            JOptionPane.INFORMATION_MESSAGE
        )
    }
}