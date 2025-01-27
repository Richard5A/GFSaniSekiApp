# Gymnasium Freiham Sanitätsdienst Alarmierungsapp für das Sekretariat.
Dies ist eine einfache App zur Alarmierung von Schulsanitätern über die FF-Agent-API. Bisher funktioniert diese nur für die Free-Version von FF-Agent.

![grafik](https://github.com/user-attachments/assets/83e33f81-42fc-4274-bb48-48d03484d5f4)
![grafik](https://github.com/user-attachments/assets/faafa8f8-4a1c-4cbc-8f12-cc453233e64e)


## Datenschutz
Es werden Zugangsdaten wie z.B. der Web-API-Token unverschlüsselt auf dem lokalen Gerät gespeichert.
Zusätzlich werden auch Ortsnamen von vordefinierten Einsatzorten und deren Koordinaten unverschlüsselt im gleichen Ordner gespeichert.

## Installation
Es können `.dmg`, `.msi` und `.deb`-Installer erstellt werden. 
Für Windows ist eingestellt, dass eine Verknüpfung auf dem Desktop erzeugt und nur für den ausführenden Nutzer installiert werden soll. Dadurch sind bei der Installation keine Administratoren-Rechte notwendig.
```kts
nativeDistributions {
    targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
    vendor = "Richard5A"
    //...
    windows.iconFile.set(layout.projectDirectory.file("src/main/resources/logo.ico"))
    windows.perUserInstall = true
    windows.shortcut = true
}

```

## Funktionen
- Automatisches Mapping von Einsatzort-Namen zu deren Koordinaten. Siehe hierzu: [places.json](#placesjson)
- Hinzu alarmieren von Leader auf Knopfdruck Siehe hierzu: [config.json](#configjson)
- Schnelles Startup durch laufen im Hintergrund.
- Switchen zwischen Live und Free mode.

### `ffagent-keycert.p12`
Diese Datei wird beim Abschließen eines Abos bei FF-Agent von FF-Agent bereitgestellt und muss in das Hauptverzeichnis gelegt werden. Dadurch wird die Verbindung zwischen FF-Agent Server und Client gesichert. Weiter infos siehe FF-Agent-Web-Api Dokumentation.

### `config.json`
In dieser Datei werden einfache Konfigurationen gespeichert. Wichtig, wenn man diese Funktion nutzen möchte, muss man in der FF-Agent Web UI eine extra Alarmbenachrichtigung für diese Person anlegen. Das Keyword wird als  `<AUSGEWÄTES KEYWORD> + K` übergeben.
Auch dies muss man manuell konfigurieren. Siehe hierzu die FF-Agent Web UI Dokumentation. Das Password für die p12 Datei muss manuell konfiguriert werden.

Format:
```json
{
  "leaderName": "Max Mustermann",
  "isFreeVersion": true,
  "certificatePassword": "..."
}
```

### `places.json`
In dieser Datei werden die vorkonfigurierten Einsatzorte für die UI hinterlegt. Diese muss von Hand erstellt und ausgefüllt werden. Wenn diese weggelassen wird, findet das Mapping nicht statt.

Format:
```json
[
  {
    "name": "C3.03.03",
    "aliases": [],
    "lat": 48.000,
    "lng": 11.000,
    "description": null
  },
  {
    "name": "C1.00.10",
    "aliases": [
      "...",
      "..."
    ],
    "lat": 48.000,
    "lng": 11.000,
    "description": null
  }
]
```

### `credentials.json`
In dieser Datei werden die Zugangsdaten für die FF-Agent-API hinterlegt. Wenn keine Datei gefunden wird, werden beim Start des Programms die Zugangsdaten abgefragt. Die Tokens werden aus FF-Agent Web-UI herauskopiert. (Können nur Administratoren)

Format:
```json
{
  "webApiToken": "...",
  "accessToken": "...", 
  "selectiveCallCode": "...", //Schleifenname aus Soft-Gateway Anschluss
  "webApiKey": "..."
}
```

### `debug.txt`
Wenn in dieser Datei der Wert `true` steht, ist der Debug-Modus aktiviert. Dadurch kann man auch ein Keyword angeben und somit auch Testalarme auslösen. Zukünftig werden auch weitere Funktionen folgen. 

Format:
```
true
```
