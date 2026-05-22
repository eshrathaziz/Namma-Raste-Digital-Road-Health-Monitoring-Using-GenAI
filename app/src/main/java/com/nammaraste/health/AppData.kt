package com.nammaraste.health

import android.content.Context

data class Road(
    val id: Int,
    val name: String,
    val nameKn: String,
    val zone: String,
    val zoneKn: String,
    val health: Int,
    val contractor: String,
    val warranty: String,
    val reports: MutableList<String>,
    val length: String,
    val lat: Double,
    val lng: Double
)

data class Contractor(
    val name: String,
    val code: String,
    val roads: List<String>,
    val warranty: Int,
    val rating: Float,
    val reviews: Int,
    val phone: String,
    val email: String
)

object AppData {

    val roads = mutableListOf(
        Road(1, "Outer Ring Road (ORR)", "ಹೊರ ವರ್ತುಲ ರಸ್ತೆ", "Mahadevapura", "ಮಹಾದೇವಪುರ", 88, "L&T Smart World", "2027-06", mutableListOf(), "28km", 12.9568, 77.7011),
        Road(2, "Indiranagar 100ft Road", "ಇಂದಿರಾನಗರ 100 ಅಡಿ ರಸ್ತೆ", "East Zone", "ಪೂರ್ವ ವಲಯ", 74, "BDA Constructions", "2026-03", mutableListOf(), "6km", 12.9784, 77.6408),
        Road(3, "Silk Board Junction", "ಸಿಲ್ಕ್ ಬೋರ್ಡ್ ಜಂಕ್ಷನ್", "Bommanahalli", "ಬೊಮ್ಮನಹಳ್ಳಿ", 42, "KNR Constructions", "2025-12", mutableListOf(), "4km", 12.9177, 77.6238),
        Road(4, "Whitefield Main Road", "ವೈಟ್‌ಫೀಲ್ಡ್ ಮುಖ್ಯ ರಸ್ತೆ", "Mahadevapura", "ಮಹಾದೇವಪುರ", 29, "NCC Infra Ltd", "2025-06", mutableListOf(), "12km", 12.9698, 77.7500),
        Road(5, "Koramangala 80ft Road", "ಕೋರಮಂಗಲ 80 ಅಡಿ ರಸ್ತೆ", "South Zone", "ದಕ್ಷಿಣ ವಲಯ", 91, "L&T Smart World", "2027-09", mutableListOf(), "5km", 12.9352, 77.6245),
        Road(6, "Bannerghatta Road", "ಬನ್ನೇರುಘಟ್ಟ ರಸ್ತೆ", "South Zone", "ದಕ್ಷಿಣ ವಲಯ", 65, "Gayathri Projects", "2026-01", mutableListOf(), "14km", 12.8876, 77.5970),
        Road(7, "Hebbal Flyover", "ಹೆಬ್ಬಾಳ ಮೇಲ್ಸೇತುವೆ", "North Zone", "ಉತ್ತರ ವಲಯ", 81, "Navayuga Engineering", "2027-03", mutableListOf(), "3km", 13.0358, 77.5970),
        Road(8, "Mysore Road (NH-275)", "ಮೈಸೂರು ರಸ್ತೆ", "West Zone", "ಪಶ್ಚಿಮ ವಲಯ", 55, "IRB Infra", "2025-09", mutableListOf(), "32km", 12.9516, 77.5185),
        Road(9, "MG Road", "ಎಂ.ಜಿ. ರಸ್ತೆ", "East Zone", "ಪೂರ್ವ ವಲಯ", 95, "BBMP Special", "2028-01", mutableListOf(), "2km", 12.9756, 77.6067),
        Road(10, "Brigade Road", "ಬ್ರಿಗೇಡ್ ರಸ್ತೆ", "East Zone", "ಪೂರ್ವ ವಲಯ", 92, "BBMP Special", "2027-12", mutableListOf(), "1.5km", 12.9734, 77.6068),
        Road(11, "Residency Road", "ರೆಸಿಡೆನ್ಸಿ ರಸ್ತೆ", "Central", "ಕೇಂದ್ರ", 78, "BDA", "2026-08", mutableListOf(), "3km", 12.9698, 77.6000),
        Road(12, "Hosur Road", "ಹೊಸೂರು ರಸ್ತೆ", "South Zone", "ದಕ್ಷಿಣ ವಲಯ", 62, "NHAI", "2025-10", mutableListOf(), "15km", 12.9082, 77.6322),
        Road(13, "Old Airport Road", "ಹಳೆಯ ವಿಮಾನ ನಿಲ್ದಾಣ ರಸ್ತೆ", "East Zone", "ಪೂರ್ವ ವಲಯ", 70, "L&T", "2026-11", mutableListOf(), "10km", 12.9599, 77.6644),
        Road(14, "Sarjapur Road", "ಸರ್ಜಾಪುರ ರಸ್ತೆ", "Mahadevapura", "ಮಹಾದೇವಪುರ", 48, "NCC", "2025-07", mutableListOf(), "14km", 12.9116, 77.6747),
        Road(15, "Bellary Road", "ಬಳ್ಳಾರಿ ರಸ್ತೆ", "North Zone", "ಉತ್ತರ ವಲಯ", 85, "Navayuga", "2027-04", mutableListOf(), "20km", 13.0180, 77.5891),
        Road(16, "Kanakapura Road", "ಕನಕಪುರ ರಸ್ತೆ", "South Zone", "ದಕ್ಷಿಣ ವಲಯ", 58, "NHAI", "2025-12", mutableListOf(), "18km", 12.8715, 77.5458),
        Road(17, "Old Madras Road", "ಹಳೆಯ ಮದ್ರಾಸ್ ರಸ್ತೆ", "East Zone", "ಪೂರ್ವ ವಲಯ", 72, "IRB", "2026-02", mutableListOf(), "12km", 12.9850, 77.6950),
        Road(18, "Tumkur Road", "ತುಮಕೂರು ರಸ್ತೆ", "West Zone", "ಪಶ್ಚಿಮ ವಲಯ", 66, "L&T", "2025-09", mutableListOf(), "25km", 13.0300, 77.4800),
        Road(19, "Richmond Road", "ರಿಚ್ಮಂಡ್ ರಸ್ತೆ", "Central", "ಕೇಂದ್ರ", 80, "BDA", "2027-01", mutableListOf(), "4km", 12.9647, 77.5950),
        Road(20, "Cunningham Road", "ಕನ್ನಿಂಗ್ಹ್ಯಾಮ್ ರಸ್ತೆ", "Central", "ಕೇಂದ್ರ", 88, "BBMP", "2027-05", mutableListOf(), "2km", 12.9840, 77.5940),
        Road(21, "Vittal Mallya Road", "ವಿಠ್ಠಲ್ ಮಲ್ಯ ರಸ್ತೆ", "Central", "ಕೇಂದ್ರ", 94, "BBMP Special", "2028-06", mutableListOf(), "1.5km", 12.9710, 77.5960),
        Road(22, "St. Marks Road", "ಸೇಂಟ್ ಮಾರ್ಕ್ಸ್ ರಸ್ತೆ", "Central", "ಕೇಂದ್ರ", 91, "BBMP", "2027-11", mutableListOf(), "2km", 12.9740, 77.6010),
        Road(23, "BTM 100ft Road", "ಬಿಟಿಎಂ 100 ಅಡಿ ರಸ್ತೆ", "South Zone", "ದಕ್ಷಿಣ ವಲಯ", 76, "BDA", "2026-09", mutableListOf(), "5km", 12.9160, 77.6050),
        Road(24, "Jayanagar 4th Block", "ಜಯನಗರ 4ನೇ ಬ್ಲಾಕ್", "South Zone", "ದಕ್ಷಿಣ ವಲಯ", 89, "BBMP", "2027-08", mutableListOf(), "3km", 12.9290, 77.5830),
        Road(25, "HSR 27th Main", "ಎಚ್‌ಎಸ್‌ಆರ್ 27ನೇ ಮುಖ್ಯ ರಸ್ತೆ", "Bommanahalli", "ಬೊಮ್ಮನಹಳ್ಳಿ", 82, "BDA", "2027-03", mutableListOf(), "4km", 12.9130, 77.6500),
        Road(26, "Commercial Street", "ಕಾಮರ್ಷಿಯಲ್ ಸ್ಟ್ರೀಟ್", "East Zone", "ಪೂರ್ವ ವಲಯ", 93, "BBMP Special", "2028-05", mutableListOf(), "1km", 12.9822, 77.6083),
        Road(27, "Lavelle Road", "ಲಾವೆಲ್ಲೆ ರಸ್ತೆ", "Central", "ಕೇಂದ್ರ", 90, "BBMP", "2027-10", mutableListOf(), "2km", 12.9710, 77.5990),
        Road(28, "Double Road (Indiranagar)", "ಡಬಲ್ ರಸ್ತೆ", "East Zone", "ಪೂರ್ವ ವಲಯ", 75, "BDA", "2026-04", mutableListOf(), "3km", 12.9780, 77.6380),
        Road(29, "80 Feet Road (Indiranagar)", "80 ಅಡಿ ರಸ್ತೆ", "East Zone", "ಪೂರ್ವ ವಲಯ", 82, "L&T", "2027-02", mutableListOf(), "4km", 12.9720, 77.6420),
        Road(30, "Kasturba Road", "ಕಸ್ತೂರಿಬಾ ರಸ್ತೆ", "Central", "ಕೇಂದ್ರ", 87, "BBMP", "2027-07", mutableListOf(), "2.5km", 12.9750, 77.5960),
        Road(31, "Museum Road", "ಮ್ಯೂಸಿಯಂ ರಸ್ತೆ", "Central", "ಕೇಂದ್ರ", 84, "BBMP", "2026-12", mutableListOf(), "1.5km", 12.9720, 77.6020),
        Road(32, "Cubbon Road", "ಕಬ್ಬನ್ ರಸ್ತೆ", "Central", "ಕೇಂದ್ರ", 89, "BBMP", "2027-09", mutableListOf(), "3km", 12.9810, 77.6000),
        Road(33, "Infantry Road", "ಇನ್ಫಾಂಟ್ರಿ ರಸ್ತೆ", "Central", "ಕೇಂದ್ರ", 77, "BDA", "2026-06", mutableListOf(), "2km", 12.9830, 77.6020),
        Road(34, "Kammanahalli Main Road", "ಕಮ್ಮನಹಳ್ಳಿ ಮುಖ್ಯ ರಸ್ತೆ", "East Zone", "ಪೂರ್ವ ವಲಯ", 68, "NCC", "2025-11", mutableListOf(), "4km", 13.0160, 77.6380),
        Road(35, "Hennur Main Road", "ಹೆಣ್ಣೂರು ಮುಖ್ಯ ರಸ್ತೆ", "East Zone", "ಪೂರ್ವ ವಲಯ", 59, "IRB", "2025-08", mutableListOf(), "8km", 13.0250, 77.6280),
        Road(36, "Thanisandra Main Road", "ತನಿಷಂದ್ರ ಮುಖ್ಯ ರಸ್ತೆ", "North Zone", "ಉತ್ತರ ವಲಯ", 64, "Navayuga", "2026-01", mutableListOf(), "6km", 13.0550, 77.6250),
        Road(37, "New BEL Road", "ನ್ಯೂ ಬಿಇಎಲ್ ರಸ್ತೆ", "North Zone", "ಉತ್ತರ ವಲಯ", 83, "L&T", "2027-03", mutableListOf(), "3km", 13.0300, 77.5750),
        Road(38, "Sanjay Nagar Main Road", "ಸಂಜಯ್ ನಗರ ಮುಖ್ಯ ರಸ್ತೆ", "North Zone", "ಉತ್ತರ ವಲಯ", 79, "BDA", "2026-10", mutableListOf(), "4km", 13.0350, 77.5800),
        Road(39, "R.T. Nagar Main Road", "ಆರ್.ಟಿ. ನಗರ ಮುಖ್ಯ ರಸ್ತೆ", "North Zone", "ಉತ್ತರ ವಲಯ", 72, "BBMP", "2026-05", mutableListOf(), "3km", 13.0120, 77.5920),
        Road(40, "Jaymahal Road", "ಜಯಮಹಲ್ ರಸ್ತೆ", "North Zone", "ಉತ್ತರ ವಲಯ", 86, "BBMP", "2027-08", mutableListOf(), "2km", 12.9980, 77.5950),
        Road(41, "Millers Road", "ಮಿಲ್ಲರ್ಸ್ ರಸ್ತೆ", "Central", "ಕೇಂದ್ರ", 81, "BBMP", "2027-04", mutableListOf(), "2.5km", 12.9920, 77.5980),
        Road(42, "Queens Road", "ಕ್ವೀನ್ಸ್ ರಸ್ತೆ", "Central", "ಕೇಂದ್ರ", 88, "BBMP", "2027-07", mutableListOf(), "1.5km", 12.9870, 77.5980),
        Road(43, "Varthur Road", "ವರ್ತೂರು ರಸ್ತೆ", "Mahadevapura", "ಮಹಾದೇವಪುರ", 45, "NCC", "2025-06", mutableListOf(), "10km", 12.9550, 77.7250),
        Road(44, "Panathur Main Road", "ಪನತ್ತೂರು ಮುಖ್ಯ ರಸ್ತೆ", "Mahadevapura", "ಮಹಾದೇವಪುರ", 38, "NCC", "2025-03", mutableListOf(), "5km", 12.9350, 77.6950),
        Road(45, "Balagere Road", "ಬಾಲಗೆರೆ ರಸ್ತೆ", "Mahadevapura", "ಮಹಾದೇವಪುರ", 41, "NCC", "2025-04", mutableListOf(), "4km", 12.9400, 77.7150),
        Road(46, "Nallurahalli Main Road", "ನಲ್ಲೂರಹಳ್ಳಿ ಮುಖ್ಯ ರಸ್ತೆ", "Mahadevapura", "ಮಹಾದೇವಪುರ", 52, "NCC", "2025-09", mutableListOf(), "3km", 12.9700, 77.7300),
        Road(47, "ITPL Main Road", "ಐಟಿಪಿಎಲ್ ಮುಖ್ಯ ರಸ್ತೆ", "Mahadevapura", "ಮಹಾದೇವಪುರ", 67, "L&T", "2026-02", mutableListOf(), "6km", 12.9850, 77.7400),
        Road(48, "Graphite India Road", "ಗ್ರ್ಯಾಫೈಟ್ ಇಂಡಿಯಾ ರಸ್ತೆ", "Mahadevapura", "ಮಹಾದೇವಪುರ", 55, "BDA", "2025-11", mutableListOf(), "4km", 12.9800, 77.7150),
        Road(49, "Brookefield Main Road", "ಬ್ರೂಕ್‌ಫೀಲ್ಡ್ ಮುಖ್ಯ ರಸ್ತೆ", "Mahadevapura", "ಮಹಾದೇವಪುರ", 61, "BDA", "2025-12", mutableListOf(), "3km", 12.9650, 77.7150),
        Road(50, "Marathahalli Bridge Road", "ಮರತಹಳ್ಳಿ ಬ್ರಿಡ್ಜ್ ರಸ್ತೆ", "Mahadevapura", "ಮಹಾದೇವಪುರ", 50, "BBMP", "2025-07", mutableListOf(), "2km", 12.9550, 77.6980)
    )

    val contractors = listOf(
        Contractor("L&T Smart World", "KA-BBMP-2022-04", listOf("Outer Ring Road (ORR)", "Koramangala 80ft Road", "Old Airport Road", "80 Feet Road (Indiranagar)", "New BEL Road", "ITPL Main Road"), 85, 4.7f, 42, "9845001234", "lt.smart@bbmp.gov.in"),
        Contractor("BDA Constructions", "KA-BDA-2021-11", listOf("Indiranagar 100ft Road", "Residency Road", "Richmond Road", "BTM 100ft Road", "HSR 27th Main", "Infantry Road", "Sanjay Nagar Main Road", "Double Road (Indiranagar)", "Graphite India Road", "Brookefield Main Road"), 55, 3.9f, 28, "9876543210", "bda.con@gov.in"),
        Contractor("KNR Constructions", "KA-NHAI-2020-07", listOf("Silk Board Junction"), 22, 3.1f, 53, "9741238765", "knr.blr@nhai.gov.in"),
        Contractor("NCC Infra Ltd", "KA-BBMP-2019-03", listOf("Whitefield Main Road", "Sarjapur Road", "Kammanahalli Main Road", "Varthur Road", "Panathur Main Road", "Balagere Road", "Nallurahalli Main Road"), 8, 2.2f, 71, "9900112233", "ncc.wf@bbmp.gov.in"),
        Contractor("Gayathri Projects", "KA-BBMP-2021-09", listOf("Bannerghatta Road"), 40, 3.6f, 22, "9988776655", "gayathri.bg@bbmp.gov.in"),
        Contractor("Navayuga Engineering", "KA-NHAI-2022-01", listOf("Hebbal Flyover", "Bellary Road", "Thanisandra Main Road"), 78, 4.4f, 35, "9871234567", "navayuga.hbl@nhai.gov.in"),
        Contractor("IRB Infra", "KA-NHAI-2020-05", listOf("Mysore Road (NH-275)", "Old Madras Road", "Kanakapura Road", "Tumkur Road", "Hennur Main Road"), 30, 3.3f, 48, "9654321098", "irb.mys@nhai.gov.in"),
        Contractor("BBMP Special Division", "KA-BBMP-SPEC-01", listOf("MG Road", "Brigade Road", "Vittal Mallya Road", "St. Marks Road", "Jayanagar 4th Block", "Cunningham Road", "Commercial Street", "Lavelle Road", "Kasturba Road", "Museum Road", "Cubbon Road", "Queens Road", "Jaymahal Road", "Millers Road", "R.T. Nagar Main Road"), 92, 4.8f, 65, "080-22221234", "special.div@bbmp.gov.in")
    )

    fun init(context: Context) {}

    fun healthClass(h: Int): String = when {
        h >= 80 -> "good"
        h >= 55 -> "warn"
        else -> "bad"
    }

    fun healthLabel(h: Int): String = when {
        h >= 80 -> "Healthy"
        h >= 55 -> "Moderate"
        else -> "Critical"
    }

    fun healthLabelKn(h: Int): String = when {
        h >= 80 -> "ಉತ್ತಮ"
        h >= 55 -> "ಸಾಧಾರಣ"
        else -> "ನಿರ್ಣಾಯಕ"
    }
}
