package uk.co.platosys.pws.constants;
import java.util.* ;
import java.io.Serializable ;

import uk.co.platosys.pws.values.BasicValuePair;
import uk.co.platosys.pws.values.ValuePair;

/** Class to list nations and nationalities*/
public class Nations implements Serializable {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static Map<String, String> nations = new HashMap<String, String>();
	static Map<String, String> nationalities = new HashMap<String, String>();
	
		public static final String VU = "Vanuatu";
		public static final String VUY = "Vanuatuan";
		public static final String VN = "Vietnam";
		public static final String VNY = "Vietnamese";
		public static final String EC = "Ecuador";
		public static final String ECY = "Ecuadorian";
		public static final String VI = "U.S. Virgin Islands";
		public static final String VIY = "US Virgin Islander";
		public static final String DZ = "Algeria";
		public static final String DZY = "Algerian";
		public static final String VG = "British Virgin Islands";
		public static final String VGY = "British Virgin Islander";
		public static final String VE = "Venezuela";
		public static final String VEY = "Venezuelan";
		public static final String VC = "Saint Vincent and the Grenadines";
		public static final String VCY = "Vincentian";
		public static final String DO = "Dominican Republic";
		public static final String DOY = "Dominican";
		public static final String VA = "Vatican City";
		public static final String VAY = "Vatican";
		public static final String DE = "Germany";
		public static final String DEY = "German";
		public static final String UZ = "Uzbekistan";
		public static final String UZY = "Uzbek";
		public static final String UY = "Uruguay";
		public static final String UYY = "Uruguayan";
		public static final String DK = "Denmark";
		public static final String DKY = "Danish";
		public static final String DJ = "Djibouti";
		public static final String DJY = "Djiboutian";
		public static final String US = "United States";
		public static final String USY = "American";
		public static final String UM = "U.S. Minor Outlying Islands";
		public static final String UMY = "United States Minor Outlying Islands";
		public static final String UG = "Uganda";
		public static final String UGY = "Ugandan";
		public static final String UA = "Ukraine";
		public static final String UAY = "Ukrainian";
		public static final String ET = "Ethiopia";
		public static final String ETY = "Ethiopian";
		public static final String ES = "Spain";
		public static final String ESY = "Spaniard";
		public static final String ER = "Eritrea";
		public static final String ERY = "Eritrean";
		public static final String EH = "Western Sahara";
		public static final String EHY = "Sahrawi";
		public static final String EG = "Egypt";
		public static final String EGY = "Egyptian";
		public static final String TZ = "Tanzania";
		public static final String TZY = "Tanzanian";
		public static final String EE = "Estonia";
		public static final String EEY = "Estonian";
		public static final String TT = "Trinidad and Tobago";
		public static final String TTY = "Trinidad and Tobago national";
		public static final String TW = "Taiwan";
		public static final String TWY = "Taiwanese";
		public static final String TV = "Tuvalu";
		public static final String TVY = "Tuvaluan";
		public static final String GD = "Grenada";
		public static final String GDY = "Grenadian";
		public static final String GE = "Georgia";
		public static final String GEY = "Georgian";
		public static final String GF = "French Guiana";
		public static final String GFY = "Guianese";
		public static final String GA = "Gabon";
		public static final String GAY = "Gabonese";
		public static final String GB = "United Kingdom";
		public static final String GBY = "British";
		public static final String FR = "France";
		public static final String FRY = "French";
		public static final String FO = "Faroe Islands";
		public static final String FOY = "Faeroese";
		public static final String FK = "Falkland Islands";
		public static final String FKY = "Falkland Islander";
		public static final String FJ = "Fiji";
		public static final String FJY = "Fiji Islander";
		public static final String FM = "Micronesia";
		public static final String FMY = "Micronesian";
		public static final String FI = "Finland";
		public static final String FIY = "Finn";
		public static final String WS = "Samoa";
		public static final String WSY = "Samoan";
		public static final String GY = "Guyana";
		public static final String GYY = "Guyanese";
		public static final String GW = "Guinea-Bissau";
		public static final String GWY = "Guinea-Bissau national";
		public static final String GU = "Guam";
		public static final String GUY = "Guamanian";
		public static final String GT = "Guatemala";
		public static final String GTY = "Guatemalan";
		public static final String GS = "South Georgia and the South Sandwich Islands";
		public static final String GSY = "South Georgia and the South Sandwich Islands";
		public static final String GR = "Greece";
		public static final String GRY = "Greek";
		public static final String GQ = "Equatorial Guinea";
		public static final String GQY = "Equatorial Guinean";
		public static final String WF = "Wallis and Futuna";
		public static final String WFY = "Wallis and Futuna Islander";
		public static final String GP = "Guadeloupe";
		public static final String GPY = "Guadeloupean";
		public static final String GN = "Guinea";
		public static final String GNY = "Guinean";
		public static final String GM = "Gambia";
		public static final String GMY = "Gambian";
		public static final String GL = "Greenland";
		public static final String GLY = "Greenlander";
		public static final String GI = "Gibraltar";
		public static final String GIY = "Gibraltarian";
		public static final String GH = "Ghana";
		public static final String GHY = "Ghanaian";
		public static final String RE = "Réunion";
		public static final String REY = "Reunionese";
		public static final String RO = "Romania";
		public static final String ROY = "Romanian";
		public static final String AT = "Austria";
		public static final String ATY = "Austrian";
		public static final String AS = "American Samoa";
		public static final String ASY = "American Samoan";
		public static final String AR = "Argentina";
		public static final String ARY = "Argentinian";
		public static final String AQ = "Antarctica";
		public static final String AQY = "Antarctican";
		public static final String AX = "Åland";
		public static final String AXY = "Åland Islander";
		public static final String AW = "Aruba";
		public static final String AWY = "Aruban";
		public static final String QA = "Qatar";
		public static final String QAY = "Qatari";
		public static final String AU = "Australia";
		public static final String AUY = "Australian";
		public static final String AZ = "Azerbaijan";
		public static final String AZY = "Azerbaijani";
		public static final String BA = "Bosnia and Herzegovina";
		public static final String BAY = "Bosnia and Herzegovina national";
		public static final String PT = "Portugal";
		public static final String PTY = "Portuguese";
		public static final String AD = "Andorra";
		public static final String ADY = "Andorran";
		public static final String PW = "Palau";
		public static final String PWY = "Palauan";
		public static final String AG = "Antigua and Barbuda";
		public static final String AGY = "Antigua and Barbuda national";
		public static final String AE = "United Arab Emirates";
		public static final String AEY = "Emirian";
		public static final String PR = "Puerto Rico";
		public static final String PRY = "Puerto Rican";
		public static final String AF = "Afghanistan";
		public static final String AFY = "Afghan";
		public static final String AL = "Albania";
		public static final String ALY = "Albanian";
		public static final String AI = "Anguilla";
		public static final String AIY = "Anguillan";
		public static final String AO = "Angola";
		public static final String AOY = "Angolan";
		public static final String PY = "Paraguay";
		public static final String PYY = "Paraguayan";
		public static final String AM = "Armenia";
		public static final String AMY = "Armenian";
		public static final String TG = "Togo";
		public static final String TGY = "Togolese";
		public static final String BW = "Botswana";
		public static final String BWY = "Botswanan";
		public static final String TF = "French Southern Territories";
		public static final String TFY = "French Southern Territories";
		public static final String BV = "Bouvet Island";
		public static final String BVY = "Bouvet Island";
		public static final String BY = "Belarus";
		public static final String BYY = "Belarusian";
		public static final String TD = "Chad";
		public static final String TDY = "Chadian";
		public static final String TK = "Tokelau";
		public static final String TKY = "Tokelauan";
		public static final String BS = "Bahamas";
		public static final String BSY = "Bahamian";
		public static final String TJ = "Tajikistan";
		public static final String TJY = "Tajik";
		public static final String BR = "Brazil";
		public static final String BRY = "Brazilian";
		public static final String TH = "Thailand";
		public static final String THY = "Thai";
		public static final String BT = "Bhutan";
		public static final String BTY = "Bhutanese";
		public static final String TO = "Tonga";
		public static final String TOY = "Tongan";
		public static final String TN = "Tunisia";
		public static final String TNY = "Tunisian";
		public static final String TM = "Turkmenistan";
		public static final String TMY = "Turkmen";
		public static final String TL = "East Timor";
		public static final String TLY = "East Timorese";
		public static final String CA = "Canada";
		public static final String CAY = "Canadian";
		public static final String TR = "Turkey";
		public static final String TRY = "Turk";
		public static final String BZ = "Belize";
		public static final String BZY = "Belizean";
		public static final String BF = "Burkina Faso";
		public static final String BFY = "Burkinabe";
		public static final String BG = "Bulgaria";
		public static final String BGY = "Bulgarian";
		public static final String SV = "El Salvador";
		public static final String SVY = "Salvadorian";
		public static final String BH = "Bahrain";
		public static final String BHY = "Bahraini";
		public static final String BI = "Burundi";
		public static final String BIY = "Burundian";
		public static final String ST = "São Tomé and Príncipe";
		public static final String STY = "São Toméan";
		public static final String SY = "Syria";
		public static final String SYY = "Syrian";
		public static final String BB = "Barbados";
		public static final String BBY = "Barbadian";
		public static final String SZ = "Swaziland";
		public static final String SZY = "Swazi";
		public static final String BD = "Bangladesh";
		public static final String BDY = "Bangladeshi";
		public static final String BE = "Belgium";
		public static final String BEY = "Belgian";
		public static final String BN = "Brunei";
		public static final String BNY = "Bruneian";
		public static final String BO = "Bolivia";
		public static final String BOY = "Bolivian";
		public static final String BJ = "Benin";
		public static final String BJY = "Beninese";
		public static final String TC = "Turks and Caicos Islands";
		public static final String TCY = "Turks and Caicos Islander";
		public static final String BM = "Bermuda";
		public static final String BMY = "Bermudian";
		public static final String SD = "Sudan";
		public static final String SDY = "Sudanese";
		public static final String CZ = "Czech Republic";
		public static final String CZY = "Czech";
		public static final String CY = "Cyprus";
		public static final String CYY = "Cypriot";
		public static final String SC = "Seychelles";
		public static final String SCY = "Seychellois";
		public static final String CX = "Christmas Island";
		public static final String CXY = "Christmas Islander";
		public static final String SE = "Sweden";
		public static final String SEY = "Swede";
		public static final String CV = "Cape Verde";
		public static final String CVY = "Cape Verdean";
		public static final String SH = "Saint Helena";
		public static final String SHY = "Saint Helenian";
		public static final String CU = "Cuba";
		public static final String CUY = "Cuban";
		public static final String SG = "Singapore";
		public static final String SGY = "Singaporean";
		public static final String SJ = "Svalbard and Jan Mayen";
		public static final String SJY = "Svalbard and Jan Mayen";
		public static final String SI = "Slovenia";
		public static final String SIY = "Slovene";
		public static final String SL = "Sierra Leone";
		public static final String SLY = "Sierra Leonean";
		public static final String SK = "Slovakia";
		public static final String SKY = "Slovak";
		public static final String SN = "Senegal";
		public static final String SNY = "Senegalese";
		public static final String SM = "San Marino";
		public static final String SMY = "San Marinese";
		public static final String SO = "Somalia";
		public static final String SOY = "Somali";
		public static final String SR = "Suriname";
		public static final String SRY = "Surinamese";
		public static final String CI = "Ivory Coast";
		public static final String CIY = "Ivorian";
		public static final String RS = "Serbia";
		public static final String RSY = "Serbian";
		public static final String CG = "Republic of the Congo";
		public static final String CGY = "Congolese";
		public static final String CH = "Switzerland";
		public static final String CHY = "Swiss";
		public static final String RU = "Russia";
		public static final String RUY = "Russian";
		public static final String CF = "Central African Republic";
		public static final String CFY = "Central African";
		public static final String RW = "Rwanda";
		public static final String RWY = "Rwandan";
		public static final String CC = "Cocos [Keeling] Islands";
		public static final String CCY = "Cocos Islander";
		public static final String CD = "Democratic Republic of the Congo";
		public static final String CDY = "Congolese";
		public static final String CR = "Costa Rica";
		public static final String CRY = "Costa Rican";
		public static final String CO = "Colombia";
		public static final String COY = "Colombian";
		public static final String CM = "Cameroon";
		public static final String CMY = "Cameroonian";
		public static final String CN = "China";
		public static final String CNY = "Chinese";
		public static final String CK = "Cook Islands";
		public static final String CKY = "Cook Islander";
		public static final String SA = "Saudi Arabia";
		public static final String SAY = "Saudi Arabian";
		public static final String SB = "Solomon Islands";
		public static final String SBY = "Solomon Islander";
		public static final String CL = "Chile";
		public static final String CLY = "Chilean";
		public static final String LV = "Latvia";
		public static final String LVY = "Latvian";
		public static final String LU = "Luxembourg";
		public static final String LUY = "Luxembourgeois";
		public static final String LT = "Lithuania";
		public static final String LTY = "Lithuanian";
		public static final String LY = "Libya";
		public static final String LYY = "Libyan";
		public static final String LS = "Lesotho";
		public static final String LSY = "Basotho";
		public static final String LR = "Liberia";
		public static final String LRY = "Liberian";
		public static final String MG = "Madagascar";
		public static final String MGY = "Malagasy";
		public static final String MH = "Marshall Islands";
		public static final String MHY = "Marshallese";
		public static final String ME = "Montenegro";
		public static final String MEY = "Montenegrian";
		public static final String MK = "Macedonia";
		public static final String MKY = "Macedonian";
		public static final String ML = "Mali";
		public static final String MLY = "Malian";
		public static final String MC = "Monaco";
		public static final String MCY = "Monegasque";
		public static final String MD = "Moldova";
		public static final String MDY = "Moldovan";
		public static final String MA = "Morocco";
		public static final String MAY = "Moroccan";
		public static final String MV = "Maldives";
		public static final String MVY = "Maldivian";
		public static final String MU = "Mauritius";
		public static final String MUY = "Mauritian";
		public static final String MX = "Mexico";
		public static final String MXY = "Mexican";
		public static final String MW = "Malawi";
		public static final String MWY = "Malawian";
		public static final String MZ = "Mozambique";
		public static final String MZY = "Mozambican";
		public static final String MY = "Malaysia";
		public static final String MYY = "Malaysian";
		public static final String MN = "Mongolia";
		public static final String MNY = "Mongolian";
		public static final String MM = "Myanmar [Burma]";
		public static final String MMY = "Burmese";
		public static final String MP = "Northern Mariana Islands";
		public static final String MPY = "Northern Mariana Islander";
		public static final String MO = "Macao";
		public static final String MOY = "Macanese";
		public static final String MR = "Mauritania";
		public static final String MRY = "Mauritanian";
		public static final String MQ = "Martinique";
		public static final String MQY = "Martinican";
		public static final String MT = "Malta";
		public static final String MTY = "Maltese";
		public static final String MS = "Montserrat";
		public static final String MSY = "Montserratian";
		public static final String NF = "Norfolk Island";
		public static final String NFY = "Norfolk Islander";
		public static final String NG = "Nigeria";
		public static final String NGY = "Nigerian";
		public static final String NI = "Nicaragua";
		public static final String NIY = "Nicaraguan";
		public static final String NL = "Netherlands";
		public static final String NLY = "Dutch";
		public static final String NA = "Namibia";
		public static final String NAY = "Namibian";
		public static final String NC = "New Caledonia";
		public static final String NCY = "New Caledonian";
		public static final String NE = "Niger";
		public static final String NEY = "Nigerien";
		public static final String NZ = "New Zealand";
		public static final String NZY = "New Zealander";
		public static final String NU = "Niue";
		public static final String NUY = "Niuean";
		public static final String NR = "Nauru";
		public static final String NRY = "Nauruan";
		public static final String NP = "Nepal";
		public static final String NPY = "Nepalese";
		public static final String NO = "Norway";
		public static final String NOY = "Norwegian";
		public static final String OM = "Oman";
		public static final String OMY = "Omani";
		public static final String PL = "Poland";
		public static final String PLY = "Pole";
		public static final String PM = "Saint Pierre and Miquelon";
		public static final String PMY = "Saint Pierre and Miquelon national";
		public static final String PN = "Pitcairn Islands";
		public static final String PNY = "Pitcairner";
		public static final String PH = "Philippines";
		public static final String PHY = "Filipino";
		public static final String PK = "Pakistan";
		public static final String PKY = "Pakistani";
		public static final String PE = "Peru";
		public static final String PEY = "Peruvian";
		public static final String PF = "French Polynesia";
		public static final String PFY = "Polynesian";
		public static final String PG = "Papua New Guinea";
		public static final String PGY = "Papua New Guinean";
		public static final String PA = "Panama";
		public static final String PAY = "Panamanian";
		public static final String HK = "Hong Kong";
		public static final String HKY = "Hong Kong Chinese";
		public static final String ZA = "South Africa";
		public static final String ZAY = "South African";
		public static final String HN = "Honduras";
		public static final String HNY = "Honduran";
		public static final String HM = "Heard Island and McDonald Islands";
		public static final String HMY = "Heard Island and McDonald Islands";
		public static final String HR = "Croatia";
		public static final String HRY = "Croatian";
		public static final String HT = "Haiti";
		public static final String HTY = "Haitian";
		public static final String HU = "Hungary";
		public static final String HUY = "Hungarian";
		public static final String ZM = "Zambia";
		public static final String ZMY = "Zambian";
		public static final String ZW = "Zimbabwe";
		public static final String ZWY = "Zimbabwean";
		public static final String ID = "Indonesia";
		public static final String IDY = "Indonesian";
		public static final String IE = "Ireland";
		public static final String IEY = "Irish";
		public static final String IL = "Israel";
		public static final String ILY = "Israeli";
		public static final String IN = "India";
		public static final String INY = "Indian";
		public static final String IO = "British Indian Ocean Territory";
		public static final String IOY = "British Indian Ocean Territory";
		public static final String IQ = "Iraq";
		public static final String IQY = "Iraqi";
		public static final String IR = "Iran";
		public static final String IRY = "Iranian";
		public static final String YE = "Yemen";
		public static final String YEY = "Yemeni";
		public static final String IS = "Iceland";
		public static final String ISY = "Icelander";
		public static final String IT = "Italy";
		public static final String ITY = "Italian";
		public static final String YT = "Mayotte";
		public static final String YTY = "Mahorais";
		public static final String JP = "Japan";
		public static final String JPY = "Japanese";
		public static final String JO = "Jordan";
		public static final String JOY = "Jordanian";
		public static final String JM = "Jamaica";
		public static final String JMY = "Jamaican";
		public static final String KI = "Kiribati";
		public static final String KIY = "Kiribatian";
		public static final String KH = "Cambodia";
		public static final String KHY = "Cambodian";
		public static final String KG = "Kyrgyzstan";
		public static final String KGY = "Kyrgyz";
		public static final String KE = "Kenya";
		public static final String KEY = "Kenyan";
		public static final String KP = "North Korea";
		public static final String KPY = "North Korean";
		public static final String KR = "South Korea";
		public static final String KRY = "South Korean";
		public static final String KM = "Comoros";
		public static final String KMY = "Comorian";
		public static final String KN = "Saint Kitts and Nevis";
		public static final String KNY = "Saint Kitts and Nevis national";
		public static final String KW = "Kuwait";
		public static final String KWY = "Kuwaiti";
		public static final String KY = "Cayman Islands";
		public static final String KYY = "Caymanian";
		public static final String KZ = "Kazakhstan";
		public static final String KZY = "Kazakh";
		public static final String LA = "Laos";
		public static final String LAY = "Lao";
		public static final String LC = "Saint Lucia";
		public static final String LCY = "Saint Lucian";
		public static final String LB = "Lebanon";
		public static final String LBY = "Lebanese";
		public static final String LI = "Liechtenstein";
		public static final String LIY = "Liechtensteiner";
		public static final String LK = "Sri Lanka";
		public static final String LKY = "Sri Lankan";
		
		public static final String [] EU= {FR, GE, IT, NE, LU, BE, DK, SE, GB, IE, ES, PT, AT, FI, GR, PL, HU,CZ, LT, LV, EE, SK, MT, HR, RO, SI, BG, CY};
		public static final String [] EUY= {FRY, GEY, ITY, NEY, LUY, BEY, DKY, SEY, GBY, IEY, ESY, PTY, ATY, FIY, GRY, PLY, HUY,CZY, LTY, LVY, EEY, SKY, MTY, HRY, ROY, SIY, BGY, CYY};
		public static final String [] EUCODES={"FR", "GE", "IT", "NE", "LU", "BE", "DK", "SE", "GB", "IE", "ES", "PT", "AT", "FI", "GR", "PL", "HU", "CZ", "LT", "LV", " EE", "SK", "MT", "HR", "RO", "SI", "BG", "CY"};
		
		public static final String [] CODES = {"VU", "VN", "EC", "VI", "DZ", "VG", "VE", "VC", "DO", "VA", "DE", "UZ", "UY", "DK", "DJ", "US", "UM", "UG", "UA", "ET", "ES", "ER", "EH", "EG", "TZ", "EE", "TT", "TW", "TV", "GD", "GE", "GF", "GA", "GB", "FR", "FO", "FK", "FJ", "FM", "FI", "WS", "GY", "GW", "GU", "GT", "GS", "GR", "GQ", "WF", "GP", "GN", "GM", "GL", "GI", "GH", "RE", "RO", "AT", "AS", "AR", "AQ", "AX", "AW", "QA", "AU", "AZ", "BA", "PT", "AD", "PW", "AG", "AE", "PR", "AF", "AL", "AI", "AO", "PY", "AM", "TG", "BW", "TF", "BV", "BY", "TD", "TK", "BS", "TJ", "BR", "TH", "BT", "TO", "TN", "TM", "TL", "CA", "TR", "BZ", "BF", "BG", "SV", "BH", "BI", "ST", "SY", "BB", "SZ", "BD", "BE", "BN", "BO", "BJ", "TC", "BM", "SD", "CZ", "CY", "SC", "CX", "SE", "CV", "SH", "CU", "SG", "SJ", "SI", "SL", "SK", "SN", "SM", "SO", "SR", "CI", "RS", "CG", "CH", "RU", "CF", "RW", "CC", "CD", "CR", "CO", "CM", "CN", "CK", "SA", "SB", "CL", "LV", "LU", "LT", "LY", "LS", "LR", "MG", "MH", "ME", "MK", "ML", "MC", "MD", "MA", "MV", "MU", "MX", "MW", "MZ", "MY", "MN", "MM", "MP", "MO", "MR", "MQ", "MT", "MS", "NF", "NG", "NI", "NL", "NA", "NC", "NE", "NZ", "NU", "NR", "NP", "NO", "OM", "PL", "PM", "PN", "PH", "PK", "PE", "PF", "PG", "PA", "HK", "ZA", "HN", "HM", "HR", "HT", "HU", "ZM", "ZW", "ID", "IE", "IL", "IN", "IO", "IQ", "IR", "YE", "IS", "IT", "YT", "JP", "JO", "JM", "KI", "KH", "KG", "KE", "KP", "KR", "KM", "KN", "KW", "KY", "KZ", "LA", "LC", "LB", "LI", "LK",   };
		public static final String [] NATIONS = {VU, VN, EC, VI, DZ, VG, VE, VC, DO, VA, DE, UZ, UY, DK, DJ, US, UM, UG, UA, ET, ES, ER, EH, EG, TZ, EE, TT, TW, TV, GD, GE, GF, GA, GB, FR, FO, FK, FJ, FM, FI, WS, GY, GW, GU, GT, GS, GR, GQ, WF, GP, GN, GM, GL, GI, GH, RE, RO, AT, AS, AR, AQ, AX, AW, QA, AU, AZ, BA, PT, AD, PW, AG, AE, PR, AF, AL, AI, AO, PY, AM, TG, BW, TF, BV, BY, TD, TK, BS, TJ, BR, TH, BT, TO, TN, TM, TL, CA, TR, BZ, BF, BG, SV, BH, BI, ST, SY, BB, SZ, BD, BE, BN, BO, BJ, TC, BM, SD, CZ, CY, SC, CX, SE, CV, SH, CU, SG, SJ, SI, SL, SK, SN, SM, SO, SR, CI, RS, CG, CH, RU, CF, RW, CC, CD, CR, CO, CM, CN, CK, SA, SB, CL, LV, LU, LT, LY, LS, LR, MG, MH, ME, MK, ML, MC, MD, MA, MV, MU, MX, MW, MZ, MY, MN, MM, MP, MO, MR, MQ, MT, MS, NF, NG, NI, NL, NA, NC, NE, NZ, NU, NR, NP, NO, OM, PL, PM, PN, PH, PK, PE, PF, PG, PA, HK, ZA, HN, HM, HR, HT, HU, ZM, ZW, ID, IE, IL, IN, IO, IQ, IR, YE, IS, IT, YT, JP, JO, JM, KI, KH, KG, KE, KP, KR, KM, KN, KW, KY, KZ, LA, LC, LB, LI, LK,   };
		public static final String [] NATIONALITIES = {VUY, VNY, ECY, VIY, DZY, VGY, VEY, VCY, DOY, VAY, DEY, UZY, UYY, DKY, DJY, USY, UMY, UGY, UAY, ETY, ESY, ERY, EHY, EGY, TZY, EEY, TTY, TWY, TVY, GDY, GEY, GFY, GAY, GBY, FRY, FOY, FKY, FJY, FMY, FIY, WSY, GYY, GWY, GUY, GTY, GSY, GRY, GQY, WFY, GPY, GNY, GMY, GLY, GIY, GHY, REY, ROY, ATY, ASY, ARY, AQY, AXY, AWY, QAY, AUY, AZY, BAY, PTY, ADY, PWY, AGY, AEY, PRY, AFY, ALY, AIY, AOY, PYY, AMY, TGY, BWY, TFY, BVY, BYY, TDY, TKY, BSY, TJY, BRY, THY, BTY, TOY, TNY, TMY, TLY, CAY, TRY, BZY, BFY, BGY, SVY, BHY, BIY, STY, SYY, BBY, SZY, BDY, BEY, BNY, BOY, BJY, TCY, BMY, SDY, CZY, CYY, SCY, CXY, SEY, CVY, SHY, CUY, SGY, SJY, SIY, SLY, SKY, SNY, SMY, SOY, SRY, CIY, RSY, CGY, CHY, RUY, CFY, RWY, CCY, CDY, CRY, COY, CMY, CNY, CKY, SAY, SBY, CLY, LVY, LUY, LTY, LYY, LSY, LRY, MGY, MHY, MEY, MKY, MLY, MCY, MDY, MAY, MVY, MUY, MXY, MWY, MZY, MYY, MNY, MMY, MPY, MOY, MRY, MQY, MTY, MSY, NFY, NGY, NIY, NLY, NAY, NCY, NEY, NZY, NUY, NRY, NPY, NOY, OMY, PLY, PMY, PNY, PHY, PKY, PEY, PFY, PGY, PAY, HKY, ZAY, HNY, HMY, HRY, HTY, HUY, ZMY, ZWY, IDY, IEY, ILY, INY, IOY, IQY, IRY, YEY, ISY, ITY, YTY, JPY, JOY, JMY, KIY, KHY, KGY, KEY, KPY, KRY, KMY, KNY, KWY, KYY, KZY, LAY, LCY, LBY, LIY, LKY,   };
		
		public static List<String> getNationCodes(){
			 List<String> codes = new ArrayList<String>();
			for(String code:CODES){
				codes.add(code);
			}
			return codes;
		}
		
		public static List<String> getNationNames(){ 
			List<String> codes = new ArrayList<String>();
			for(String code:NATIONS){
				codes.add(code);
			}
			return codes;
		}
		public static List<String> getNationalityNames(){ 
			List<String> codes = new ArrayList<String>();
			for(String code:NATIONALITIES){
				codes.add(code);
			}
			return codes;
		}
		public static Map<String, String> getNations(){  
			Map<String, String> nations = new TreeMap<String, String>();
			int i=0;
			for(String code:CODES){
			nations.put(code, NATIONS[i]);
			i++;
		}
			return nations;
		}
		public static Map<String, String> getNationsByName(){  
			Map<String, String> nations = new TreeMap<String, String>();
			int i=0;
			for(String code:CODES){
			nations.put(  NATIONS[i], code);
			i++;
		}
			return nations;
		}
		public static Map<String, String> getNationalities(){  
			Map<String, String> nations = new TreeMap<String, String>();
			int i=0;
			for(String code:CODES){
				nations.put(code, NATIONALITIES[i]);
				i++;
			}
			return nations;
		}
		public static Map<String, String> getNationalitiesByName(){  
			Map<String, String> nations = new TreeMap<String, String>();
			int i=0;
			for(String code:CODES){
				nations.put(  NATIONALITIES[i], code);
				i++;
			}
			return nations;
		}
		public static List<ValuePair> getNationPairs(){
			List<ValuePair> codes = new ArrayList<ValuePair>();
			int i=0;
			for(String code:CODES){
				ValuePair pair = new BasicValuePair( NATIONS[i], code);
				codes.add(pair);
				i++;
			}
			return codes;
		}
		public static  List<ValuePair> getNationalityPairs(){
			List<ValuePair> codes = new ArrayList<ValuePair>();
			int i=0;
			for(String code:CODES){
				ValuePair pair = new BasicValuePair( NATIONALITIES[i], code);
				codes.add(pair);
				i++;
			}
			return codes;
		}


 }