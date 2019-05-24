package com.thopham.projects.desktop.demo.domain.service
//
//import com.google.gson.Gson
//import com.thopham.projects.desktop.demo.domain.api.PrintFormAPI
//import com.thopham.projects.desktop.demo.models.printForm.DataPrint
//import org.junit.Before
//import org.junit.Test
//import org.junit.runner.RunWith
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
//
//const val dataPrintJson = "{" +
//        "      \"account\": {" +
//        "        \"name\": \"A. Khéo\"" +
//        "      }," +
//        "      \"restaurant_info\": {" +
//        "        \"name\": \"Phở Gà Ta Phố Cổ\"," +
//        "        \"phone\": \"0986147949\"," +
//        "        \"address\": \"8 Hoàng Ngọc Phách, Đống Đa, Hà Nội\"," +
//        "        \"type_print\": [" +
//        "          \"1\"," +
//        "          \"2\"," +
//        "          \"3\"," +
//        "          \"4\"," +
//        "          \"5\"," +
//        "          \"6\"," +
//        "          \"7\"," +
//        "          \"8\"," +
//        "          \"9\"," +
//        "          \"10\"," +
//        "          \"11\"," +
//        "          \"12\"," +
//        "          \"13\"" +
//        "        ]," +
//        "        \"logo\": \"https://tm.wesave.vn/cdn/restaurant/1625/web/2018/01/18/1516268299_2.phoga3_.png\"," +
//        "        \"talk\": \"Hello\"" +
//        "      }," +
//        "      \"print_tem\": true," +
//        "      \"post\": {" +
//        "        \"res_id\": 1625," +
//        "        \"id_promotion\": null," +
//        "        \"cus_name\": \"\"," +
//        "        \"telephone\": \"\"," +
//        "        \"method\": \"\"," +
//        "        \"typeorder\": \"2\"," +
//        "        \"order_type\": \"pos\"," +
//        "        \"delivery_type\": \"intable\"," +
//        "        \"detail\": [" +
//        "          {" +
//        "            \"menuID\": 13092," +
//        "            \"quantity\": 1," +
//        "            \"price\": 15000," +
//        "            \"type\": 2," +
//        "            \"principle\": 0," +
//        "            \"note\": null," +
//        "            \"size\": null," +
//        "            \"toping\": null," +
//        "            \"size_key\": null," +
//        "            \"id_opt\": 13092," +
//        "            \"special_note\": null," +
//        "            \"discount\": null," +
//        "            \"time\": 1558142371," +
//        "            \"cal_by_time\": 0," +
//        "            \"name\": \"Pepsi\"," +
//        "            \"total\": \"15,000\"," +
//        "            \"menu_id\": 3323" +
//        "          }," +
//        "          {" +
//        "            \"menuID\": 13093," +
//        "            \"quantity\": 1," +
//        "            \"price\": 30000," +
//        "            \"type\": 2," +
//        "            \"principle\": 0," +
//        "            \"note\": null," +
//        "            \"size\": null," +
//        "            \"toping\": null," +
//        "            \"size_key\": null," +
//        "            \"id_opt\": 13093," +
//        "            \"special_note\": null," +
//        "            \"discount\": null," +
//        "            \"time\": 1558142371," +
//        "            \"cal_by_time\": 0," +
//        "            \"name\": \"Bia cỏ\"," +
//        "            \"total\": \"30,000\"," +
//        "            \"menu_id\": 3323" +
//        "          }," +
//        "          {" +
//        "            \"menuID\": 14176," +
//        "            \"quantity\": 1," +
//        "            \"price\": 0," +
//        "            \"type\": 1," +
//        "            \"principle\": 0," +
//        "            \"note\": null," +
//        "            \"size\": null," +
//        "            \"toping\": null," +
//        "            \"size_key\": null," +
//        "            \"id_opt\": 14176," +
//        "            \"special_note\": null," +
//        "            \"discount\": null," +
//        "            \"time\": 1558142371," +
//        "            \"cal_by_time\": 0," +
//        "            \"name\": \"bbbb\"," +
//        "            \"total\": \"0\"," +
//        "            \"menu_id\": 3259" +
//        "          }," +
//        "          {" +
//        "            \"menuID\": 13114," +
//        "            \"quantity\": 1," +
//        "            \"price\": 0," +
//        "            \"type\": 1," +
//        "            \"principle\": 0," +
//        "            \"note\": null," +
//        "            \"size\": null," +
//        "            \"toping\": null," +
//        "            \"size_key\": null," +
//        "            \"id_opt\": 13114," +
//        "            \"special_note\": null," +
//        "            \"discount\": null," +
//        "            \"time\": 1558142371," +
//        "            \"cal_by_time\": 0," +
//        "            \"name\": \"tduy\"," +
//        "            \"total\": \"0\"," +
//        "            \"menu_id\": 3259" +
//        "          }," +
//        "          {" +
//        "            \"menuID\": 13084," +
//        "            \"quantity\": 1," +
//        "            \"price\": 300000," +
//        "            \"type\": 1," +
//        "            \"principle\": 0," +
//        "            \"note\": null," +
//        "            \"size\": null," +
//        "            \"toping\": null," +
//        "            \"size_key\": null," +
//        "            \"id_opt\": 13084," +
//        "            \"special_note\": null," +
//        "            \"discount\": null," +
//        "            \"time\": 1558142371," +
//        "            \"cal_by_time\": 0," +
//        "            \"name\": \"Bò Phố Cổ\"," +
//        "            \"total\": \"300,000\"," +
//        "            \"menu_id\": 3259" +
//        "          }" +
//        "        ]," +
//        "        \"cus_id\": -1," +
//        "        \"page_id\": 0," +
//        "        \"cus_mail\": \"\"," +
//        "        \"total_price_pay\": \"345000\"," +
//        "        \"discount\": \"\"," +
//        "        \"supplementary\": \"\"," +
//        "        \"vat\": \"\"," +
//        "        \"total_price_end\": \"345000\"," +
//        "        \"input_money\": \"345000\"," +
//        "        \"money_change\": \"\"," +
//        "        \"note\": \"\"," +
//        "        \"deliver\": 0," +
//        "        \"position\": \"Bàn Vip1\"," +
//        "        \"id_bill\": 86099," +
//        "        \"table_floor\": \"Bàn Vip1 - Tầng 1\"" +
//        "      }," +
//        "      \"full\": 1" +
//        "    }"
//@SpringBootTest
//@RunWith(SpringJUnit4ClassRunner::class)
//class PrinterServiceTest{
//    lateinit var totalDataPrint: DataPrint
//    @Autowired
//    lateinit var printFormAPI: PrintFormAPI
//    @Before
//    fun init(){
//        totalDataPrint = Gson().fromJson(dataPrintJson, DataPrint::class.java)
//    }
//    @Test
//    fun testPrintAPI(){
//        val details = totalDataPrint.post.detail
//        val detailsGrouped = details.groupBy {detail ->
//            detail.menu_id
//        }
//        for (key in detailsGrouped.keys){
//            println("Key: $key, Size = ${detailsGrouped[key]?.size ?: 0}")
//        }
//        val dataPrints = detailsGrouped.mapValues {(_, details) ->
//            totalDataPrint.copy(
//                    post = totalDataPrint.post.copy(
//                            detail = details
//                    )
//            )
//        }
//        val menuIds = detailsGrouped.keys
//        for(menuId in menuIds){
//            val dataPrint = dataPrints[menuId] ?: throw Exception("???: No way")
//            val isPrintTea = false
//            val printFormUrl = printFormAPI.fetchPrintFormUrl(dataPrint, isPrintTea)
//            println("MenuID: $menuId has URL: $printFormUrl")
//        }
//    }
//}