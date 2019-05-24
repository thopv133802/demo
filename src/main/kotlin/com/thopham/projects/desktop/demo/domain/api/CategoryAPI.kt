package com.thopham.projects.desktop.demo.domain.api

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand
import com.thopham.projects.desktop.demo.domain.api.responseModels.FetchMenuResponse
import com.thopham.projects.desktop.demo.models.Category
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@Component
class CategoryAPI(val categoryClient: CategoryClient) {
    companion object{
        private const val RESPONSE = "{\"status\":true,\"response\":{\"menu\":[{\"menuID\":-2,\"name\":\"Ph\\u1ed5 bi\\u1ebfn\",\"img\":\"\"},{\"menuID\":-1,\"name\":\"T\\u1ea5t c\\u1ea3\",\"img\":\"\"},{\"menuID\":3293,\"name\":\"Khai v\\u1ecb\",\"img\":\"https:\\/\\/cdn.wesave.vn\\/upload\\/web\\/1\\/1\\/image\\/2017\\/08\\/04\\/08\\/25\\/150183514367.jpg\"},{\"menuID\":3294,\"name\":\"Salad\",\"img\":\"https:\\/\\/cdn.wesave.vn\\/upload\\/web\\/1\\/1\\/image\\/2017\\/08\\/04\\/08\\/25\\/150183514367.jpg\"},{\"menuID\":3295,\"name\":\"G\\u00e0 kh\\u00f4ng \\u0111\\u1ed3i.\",\"img\":\"https:\\/\\/cdn.wesave.vn\\/upload\\/web\\/1\\/1\\/image\\/2017\\/08\\/04\\/08\\/25\\/150183514367.jpg\"},{\"menuID\":3296,\"name\":\"Chim c\\u00e2u\",\"img\":\"https:\\/\\/cdn.wesave.vn\\/upload\\/web\\/1\\/1\\/image\\/2017\\/08\\/04\\/08\\/25\\/150183514367.jpg\"},{\"menuID\":3297,\"name\":\"Tr\\u00e2u t\\u01b0\\u01a1i\",\"img\":\"https:\\/\\/cdn.wesave.vn\\/upload\\/web\\/1\\/1\\/image\\/2017\\/08\\/04\\/08\\/25\\/150183514367.jpg\"},{\"menuID\":3298,\"name\":\"B\\u00f2 t\\u01b0\\u01a1i\",\"img\":\"https:\\/\\/cdn.wesave.vn\\/upload\\/web\\/1\\/1\\/image\\/2017\\/08\\/04\\/08\\/25\\/150183514367.jpg\"},{\"menuID\":3299,\"name\":\"D\\u00ea t\\u01b0\\u01a1i\",\"img\":\"https:\\/\\/cdn.wesave.vn\\/upload\\/web\\/1\\/1\\/image\\/2017\\/08\\/04\\/08\\/25\\/150183514367.jpg\"},{\"menuID\":3300,\"name\":\"C\\u00e1 ninja\",\"img\":\"https:\\/\\/cdn.wesave.vn\\/upload\\/web\\/1\\/1\\/image\\/2017\\/08\\/04\\/08\\/25\\/150183514367.jpg\"},{\"menuID\":3301,\"name\":\"\\u1ed0c\",\"img\":\"https:\\/\\/cdn.wesave.vn\\/upload\\/web\\/1\\/1\\/image\\/2017\\/08\\/04\\/08\\/25\\/150183514367.jpg\"},{\"menuID\":3302,\"name\":\"\\u1ebech\",\"img\":\"https:\\/\\/cdn.wesave.vn\\/upload\\/web\\/1\\/1\\/image\\/2017\\/08\\/04\\/08\\/25\\/150183514367.jpg\"},{\"menuID\":3303,\"name\":\"M\\u1ef1c\",\"img\":\"https:\\/\\/cdn.wesave.vn\\/upload\\/web\\/1\\/1\\/image\\/2017\\/08\\/04\\/08\\/25\\/150183514367.jpg\"},{\"menuID\":3304,\"name\":\"M\\u00f3n \\u0111\\u1eb7c tr\\u01b0ng\",\"img\":\"https:\\/\\/cdn.wesave.vn\\/upload\\/web\\/1\\/1\\/image\\/2017\\/08\\/04\\/08\\/25\\/150183514367.jpg\"},{\"menuID\":3305,\"name\":\"C\\u01a1m - M\\u1ef3\",\"img\":\"https:\\/\\/cdn.wesave.vn\\/upload\\/web\\/1\\/1\\/image\\/2017\\/08\\/04\\/08\\/25\\/150183514367.jpg\"},{\"menuID\":3306,\"name\":\"C\\u01a1m - C\\u00e1c m\\u00f3n \\u0103n k\\u00e8m\",\"img\":\"https:\\/\\/cdn.wesave.vn\\/upload\\/web\\/1\\/1\\/image\\/2017\\/08\\/04\\/08\\/25\\/150183514367.jpg\"},{\"menuID\":3307,\"name\":\"Rau - \\u0110\\u1eadu\",\"img\":\"https:\\/\\/cdn.wesave.vn\\/upload\\/web\\/1\\/1\\/image\\/2017\\/08\\/04\\/08\\/25\\/150183514367.jpg\"},{\"menuID\":3308,\"name\":\"\\u0110\\u1ed3 u\\u1ed1ng\",\"img\":\"https:\\/\\/cdn.wesave.vn\\/upload\\/web\\/1\\/1\\/image\\/2017\\/08\\/04\\/08\\/25\\/150183514367.jpg\"},{\"menuID\":3501,\"name\":\"Ph\\u1edf\",\"img\":\"https:\\/\\/cdn.wesave.vn\\/upload\\/web\\/1\\/1\\/image\\/2017\\/08\\/04\\/08\\/25\\/150183514367.jpg\"},{\"menuID\":3586,\"name\":\"c\\u1edbp\",\"img\":\"https:\\/\\/cdn.wesave.vn\\/upload\\/web\\/1\\/1\\/image\\/2017\\/08\\/04\\/08\\/25\\/150183514367.jpg\"}],\"combo\":[{\"menuID\":1210,\"name\":\"Combo 1\",\"des\":\"Combo 1\",\"img\":\"https:\\/\\/book.wesave.vn\\/logows-150-new.png\",\"number_person\":12,\"price\":120009,\"imgs\":null}]}}"
    }
    fun fetchCategories(token: String, deviceId: String, restaurantId: Int): List<Category>{
        val response =  categoryClient.fetchRestaurantInformation(token, deviceId, restaurantId)
        val isFail = !response.status
        if(isFail)
            throw Exception(response.mes)
        val menus = response.response!!.menu
        val categories = mutableListOf<Category>()
        for (menu in menus){
            if(menu.id < 0) continue
            val newCategory = Category(
                    id = menu.id,
                    name = menu.name
            )
            categories.add(newCategory)
        }
        return categories
    }
//    fun fetchRegularPrintForm(data: String): String{
//        return categoryClient.fetchPrintForm(1, 0, data)
//    }
//    fun fetchPrintTeaForm(data: String): String{
//        return categoryClient.fetchPrintForm(0, 1, data)
//    }
}
@FeignClient(name = "categoryClient", url = "https://api.wesave.vn/")
interface CategoryClient{
    @GetMapping("/getInfoRes")
    @HystrixCommand(fallbackMethod = "getInfoResTimeout")
    fun fetchRestaurantInformation(@RequestParam("token") token: String, @RequestParam("device_id") deviceId: String, @RequestParam("res_id") restaurantId: Int, @RequestParam("get") get: String = "menuComboOnly"): FetchMenuResponse

    private fun getInfoResTimeout(token: String, deviceId: String, restaurantId: Int, get: String): FetchMenuResponse {
        throw Exception("Kết nối quá hạn. Vui lòng kiểm tra lại mạng và thử lại")
    }
//    @GetMapping("/printform")
//    fun fetchPrintForm(@RequestParam("print") isRegularPrint: Int = 0, @RequestParam("print_tea") isPrintTea: Int = 0, @RequestParam("data") dataEncoded: String): String
}

