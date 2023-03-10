package com.myrobi.shadhinmusiclibrary.data.repository

import android.util.Log
import com.myrobi.shadhinmusiclibrary.data.model.LoginModel
import com.myrobi.shadhinmusiclibrary.data.remote.ApiLoginService
import com.myrobi.shadhinmusiclibrary.utils.Status
import com.myrobi.shadhinmusiclibrary.utils.safeApiCall


internal class AuthRepository(private val apiLoginSer: ApiLoginService) {

    suspend fun login(msisdn: String): Pair<Boolean, String?> {
        val response = safeApiCall { apiLoginSer.fetchMyRobiLogin(LoginModel(msisdn)) }
        return if (response.status == Status.SUCCESS) {
                   appToken = response.data?.data?.Token

            Pair(true, response.message)
        } else {
            appToken = null
            Pair(false, response.message)
        }
    }

    companion object {
        var appToken: String? = null
      //  = "2+q1zStXFKcX6+jWf68dKS0mtu3DKVOTAPRmXovfezXqOqtCwHDwa36VybcTjxf0EDs+gm0edtW98wBUjTW5hPV0Msh+y+6CzMxYON1ib+srZjBfNxLpw1CdQZwLHZR9eq1lRzrrRDTavtIthP636hKFy6S/tRiFZUq8yxPCoMpIiNoTUMtxUKVDtY0BYlBVSLyGPCF50bGPh/7+isbWPZ5hKcVqQR1QplnvfSJDq1rzTNiBCjnCDY7gRvLTyv0FmfbWSFj3hU3woLXDbKPUo9f3tu7YxNF7usfn5QwDDDbucow4UCrsUbRUuogY7hyRGZjCnyWyN0dc2PLt4gmDJgxJ/oTSGue+l9/uiMKZxgN1KybHKxLExNkLvl9lf1G6sOHrmKw9SAZ3ZL9lwEt5zUhquQRwllcOT8Z5pmQAZzhWWtHW5YskUDgBBUPxOjudfxcv8ioLF9SgJvTuAM9OgythuuDwsJCKFYFGj/R3SLMmCvgXXeamyvGcxggRzCDZyQ/AauCeCNDwRxJQcq1DHffJL0jBNRggFZ89UqghWJ0svIwFPetSgNYJjTvzZCbbGynGwuQzlCu6Vq5HxT+zlmO2ezHRSVZZ1dVSIgAY3QNgXDOHhW8BSu9gTQYRrdMSQGnuzWB9ERLXyk+2AjX29Vf+L3cAGWQKKJSqoI/Hkeo6wU/ZycU7X98FeQIPZnmWKUNcsqEcg9cYL71xH27Owq3+x6J0FgI6MqcLOVNU+7rSD0Jxyxi1kMwEx4xYm81hC/L9l3drYSJHMn4JJZpvcjvRA6v5Z4LR6rMKHz8Rwc9lG70jEKy803LY5mCYF4bOKH7x6lYFZG3xf7ujIAlo2TjuQn3N3zjxGrxYKTcYNdv7lm7NTTkDqJERwMdVtkSJUUy8gn7KriKXNRNfP97+IA==:43hZ/7PdSWf2E/qOmX4YOA=="
        //TODO this time static it will dynamic
        //var appToken: String? = "I6eDy/CvlY/PoidYW7s8PKUMUjvxZBhSvJZB3YFnQutIwKxOxV1oBQCp7jVBYjR22gwn5LnzsHCUtWsQG1H/FyY580auki/I0r9DplEtuw3VHrjVgW0CVUIU4XedkBwCGksrLCXANUm8/oxgV9L2UYrmCoEzNngHMAZ0mVsob2NCHhhZag0Te+66UOu4e+JOXDPjqA7rPTVvRpvy04fR3Q4YBE9753FxCaIau3+sg2DKKg195ydXwnLVpXANQFJQy/lJkxinyWoF6otTzMC7TvurAXsi9mWcg7QzrCq4OlRWdG1SZcqjiql4NNY+NfNKu71iuKRrTXiOJBTsQK0OIJkCWgAUOceIXBWoUl7X6eFrv99iaa6W9uoTzt0meFrJcKm1+hflfs16aU7IXgSssYA3deA/V4swx8rG6oxhlOxcidkNMJ6SZxJJ078+u0gJLeiS4qr2RhmiqBxfmYrUV9bz5f+P5hS4rIu0DI3Yurqc6DIRdvVJHtx9guz8zR9phTni5W4+W9f72QBhhYUwD8VVOwG87BGT8F6kwI+OaiuKj97SuCsdYp3LmSh+0S+hYhhQPZ2SuMJ12ToiVArWs476KucK2GiZHndDLPdwslsiEX7YaRsouvt4jSEFJzQRnGCgLI0dyERlLoiG09EEnnotjzMw3rvfdlHYN2YSNwXjxKCPN98GGgiGFvKq+gHbxu9Mx9PUZSbhHRkrpGeMVYg6KRLSq0ovnlQjW+cZXd8ZmAHZokjb/sp/FAecFKQP5cwzSo/fgegZkJpd4tHqd2aN8ISJoACOWb/BDInjAlUM3PEMDkDrjQRmOJLudpwl8LAKtd26TiOUYwdt+Labd6uHE2aKm/YIL2ySyTC5Wtt5QCv9jBoisDLWJBB8/ySJakg8lNd6y+m3pgtxKzBvHA==:0BxeoDxBZz1DY+8IF0NROw=="
       // var appToken: String? =null//=//"GsgYAFMZUYmPkcmbQUnVR72xEEuiZ4PL0h2h9NHAahXrE1K/9RFwpvHN2FNB5qMkSqtGjBy+inYJj02jCDjqZCUVGcmY/HhMWeVIF0He9M8oK2zXxX5tVyw00ZKe7owtzKc53516K/3b1Y/TuSRh5VOnd2c5r/fMtPxTHPGY4LIZ9lHRdHjK8qdnx3SsniszYjdA/LIytp21vQcFGqTyCFru4h/BREyii/COeIUGP5VIBd9yQ5HjOp15olNual9az5h4RrEBc947TljJVij3K9t6DW1esZeyCdDVazHSPyaJY0/9HM6hQCTKF/ZV/ElPWi8+F6Vv+AB1b3vN+PhReR6CIwomsE6Gq1Ab71A4aPfeEmlO6xO2Zpk6G1nZJcu7fl4FfpJwbPyue8AyngnSmHqAEeJ/0d9ePDVUZ4eahN+7yKfGe94d1SYbtK61LSE+PxN5vNyBIXOiUu8sZgvlvsOez8315X6etQlCn21afZz6mtc5UfIOKE+Dg0wPtBnhS41oLUWzFhiDtQTEe8V6914/cWsYM9ISrJu0LnyVvIUcfAdiVtyoeZt1W7mYpfPZC00rVNzY5mZxBgyG2dIf9xhqSnqYBmaqWgsvlXhDF4H3BXuGnVWmKmYdAaFxBmuxgg7RthJ/JvZjxlGY7IuU4hG5iZkNWEbTlBMLmSg9JkP/vENrWG0RT0L2cq8gc/3engNSO/kUnBzI9uV4LUcLnsM+j6I/1qe124EDTDaGgmywjSNwbjq66e44m2hGmKmwnbExGyY8c95ESbe39rL1av5fTVYEvsffN1SUHOCJhmd1Ox9FhwScy8fvDmonSbUN3GMrz4q0R9uIBnxiK9rYLBftGXeaWvBa2BGKd8FnUfzKuNTtQw2hXhWq8GtOPQEibRlVliXRYqYU7PrivIBi0w==:9+3zKtIQ2jaZosto2It/vQ=="
        var msisdn:String?=null
    }
}