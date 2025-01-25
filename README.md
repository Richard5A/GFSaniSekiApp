# Gymnasium Freiham Sanitätsdienst Alarmierungsapp für das Sekreteriat.
Dies ist einen einfache App zur Alamierung von Schulsanitätern über die FF-Agent API. Bisher funktiniert diese nur für die Free-Version von FF-Agent.

![grafik](https://github.com/user-attachments/assets/83e33f81-42fc-4274-bb48-48d03484d5f4)
![grafik](https://github.com/user-attachments/assets/faafa8f8-4a1c-4cbc-8f12-cc453233e64e)


## Datenschutz
Es werden Zugangsdaten wie z.B. der Web-API-Token unverschlüsselt auf dem lokalen Gerät gespeichert.
Zusätzlich werden auch Ortsnamen von vordefinierten Einsatzorten und deren Koordinaten unverschlüsselt im gleichen Ordner gespeichrt.

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
- Automatisches Mapping von Einsatzortnamen zu deren Koordianten. Siehe hierzu: [places.json](#placesjson)
- Hinzualamieren von Leader auf Knopfdruck Siehe hierzu: [config.json](#configjson)
- Schnelles Startup durch laufen im Hintergrund.

### `config.json`
In dieseer Datei werden einfache Konfigurationen gespeichert. Zurzeit nur der Leadername. Wichtig, wenn man diese Funktion nutzen möchte, muss man in der FF-Agent Web UI eine extra Alarmbenachrichtigung für diese Person anlegen. Das Keyword wird als  `<AUSGEWÄTES KEYWORD> + K` übergeben.
Auch dies muss man manuell konfigurieren. Siehe hierzu die FF-Agent Web UI Dokumentation.

Format:
```json
{
  "leaderName": "Max Mustermann"
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

```

### `credentials.json`
In dieser Datei werden die Zugangsdaten für die FF-Agent API hinterlegt. Wenn keine Datei gefunden wird, werden beim Start des Programms die Zugangsdaten abgeefragt. Die Tokens werden aus FF-Agent Web-UI raus kopiert. (Können nur Administratoren)

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
