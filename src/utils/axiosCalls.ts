//Contains all the axios call that are needed by the application
import customAxios from "./customAxios"
import {
    ApiResponse,
    BorderCheck, GenderTypes,
    LoginCredentials,
    newPoliceCheckResult,
    newUser,
    PoliceCheckResult,
    User
} from "./model.ts";
import AsyncStorage from '@react-native-async-storage/async-storage';
import SearchResults from "../store/SearchResults.ts";


//The base url of the backend
const baseUrl = 'https://XXXXYYYYY/';

//---------------Log in section and token persistence---------------------------//
//Log in
export async function login(credentials: LoginCredentials): Promise<ApiResponse<User>> {
    //return  await customAxios.post(baseUrl + 'PLACEHOLDER', credentials).then((res) => {
        //Store the token on storage if the call is successfull
    //     try {
    //         const res = await customAxios.post(baseUrl + 'PLACEHOLDER', credentials);
    //         // Store the token if the call is successful
    //         storeToken(res.data.token);
    //         return {
    //             payload: res.data
    //         };
    //     } catch (error) {
    //         // Handle or pass the exception
    //         //Else pass the exception
    //         return {error: String(error)}
    //         //throw error; // Rethrow the error to be caught by the caller
    //     }
    // }
    return {
        payload: newUser('Telikos user', '234532', '08:00 - 19:00') //error is optional
        };
}


//Function that stores the jwt token
const storeToken = async (token) => {
    try {
        await AsyncStorage.setItem('jwtToken', token);
    } catch (error) {
        console.log('Error storing the token:', error);
    }
};
//---------------End of log in section and token persistence---------------------------//

//------------------------------Search in Police Checks------------------------------------------//
//export async function policeSearch(currentCheck: BorderCheck) : Promise<ApiResponse<PoliceCheckResult[]>> {
export async function policeSearch(currentCheck: string) : Promise<ApiResponse<PoliceCheckResult[]>> {
        // try {
        //     const res = await customAxios.post(baseUrl + 'PLACEHOLDER', currentCheck);
        //     return {
        //         payload: res.data
        //     };
        // } catch (error) {
        //     // Handle or pass the exception
        //     //Else pass the exception
        //     return { error: String(error) }
        //     //throw error; // Rethrow the error to be caught by the caller
        // }

    return {
        payload: [
            newPoliceCheckResult("1", null, 'Test11', 'Petros', 'ΓΣΔ', '15/09/2021', GenderTypes.MALE, 'Οπλοφορία' ),
            newPoliceCheckResult("2", null, 'Test22', 'Petros', 'GRC', '15/09/2021', GenderTypes.FEMALE, 'Οπλοφορία1' ),
            newPoliceCheckResult("3", null, 'Test33', 'Petros', 'ΞΒΔ', '15/09/2021', GenderTypes.MALE, 'Οπλοφορία' ),
            newPoliceCheckResult("4", null, 'Test44', 'Petros', 'GRC', '15/09/2021', GenderTypes.MALE, 'Οπλοφορία3' ),
            newPoliceCheckResult("5", null, 'Test55', 'Petros', 'ΦΤΔ', '15/09/2021', GenderTypes.FEMALE, 'Οπλοφορία4' ),
            newPoliceCheckResult("11", null, 'Test11', 'Petros', 'ΓΣΔ', '15/09/2021', GenderTypes.MALE, 'Οπλοφορία' ),
            newPoliceCheckResult("12", null, 'Test22', 'Petros', 'GRC', '15/09/2021', GenderTypes.FEMALE, 'Οπλοφορία1' ),
            newPoliceCheckResult("13", null, 'Test33', 'Petros', 'ΞΒΔ', '15/09/2021', GenderTypes.MALE, 'Οπλοφορία' ),
            newPoliceCheckResult("14", null, 'Test44', 'Petros', 'GRC', '15/09/2021', GenderTypes.MALE, 'Οπλοφορία3' ),
            newPoliceCheckResult("15", null, 'Test55', 'Petros', 'ΦΤΔ', '15/09/2021', GenderTypes.FEMALE, 'Οπλοφορία4' ),
            newPoliceCheckResult("21", null, 'Test11', 'Petros', 'ΓΣΔ', '15/09/2021', GenderTypes.MALE, 'Οπλοφορία' ),
            newPoliceCheckResult("22", null, 'Test22', 'Petros', 'GRC', '15/09/2021', GenderTypes.FEMALE, 'Οπλοφορία1' ),
            newPoliceCheckResult("23", null, 'Test33', 'Petros', 'ΞΒΔ', '15/09/2021', GenderTypes.MALE, 'Οπλοφορία' ),
            newPoliceCheckResult("24", null, 'Test44', 'Petros', 'GRC', '15/09/2021', GenderTypes.MALE, 'Οπλοφορία3' ),
            newPoliceCheckResult("25", null, 'Test55', 'Petros', 'ΦΤΔ', '15/09/2021', GenderTypes.FEMALE, 'Οπλοφορία4' )
        ]
    };
}
//------------------------------End of Search in Police Checks------------------------------------------//
//------------------------------End of Search in EES------------------------------------------//
export async function eesSearch(currentCheck: BorderCheck) : Promise<ApiResponse<PoliceCheckResult[]>> {
    // try {
    //     const res = await customAxios.post(baseUrl + 'PLACEHOLDER', currentCheck);
    //     return {
    //         payload: res.data
    //     };
    // } catch (error) {
    //     // Handle or pass the exception
    //     //Else pass the exception
    //     return { error: String(error) }
    //     //throw error; // Rethrow the error to be caught by the caller
    // }

    return {
        payload: [
            newPoliceCheckResult("1", null, 'Test11', 'Petros', 'ΓΣΔ', '15/09/2021', GenderTypes.MALE, 'Οπλοφορία' ),
            newPoliceCheckResult("2", null, 'Test22', 'Petros', 'GRC', '15/09/2021', GenderTypes.FEMALE, 'Οπλοφορία1' ),
            newPoliceCheckResult("3", null, 'Test33', 'Petros', 'ΞΒΔ', '15/09/2021', GenderTypes.MALE, 'Οπλοφορία' ),
            newPoliceCheckResult("4", null, 'Test44', 'Petros', 'GRC', '15/09/2021', GenderTypes.MALE, 'Οπλοφορία3' ),
            newPoliceCheckResult("5", null, 'Test55', 'Petros', 'ΦΤΔ', '15/09/2021', GenderTypes.FEMALE, 'Οπλοφορία4' )
        ]
    };
}
// export function axiosPost(url: string, item: Object) {
//     return  axios.post(firebaseUrl + url + '.json', item)
// }
//
// export function axiosPut(url: string, item: Object) {
//     //console.log(firebaseUrl + "/" + item.id +  '.json')
//     return  axios.put(firebaseUrl + url + "/"  + item.id +  '.json', item)
// }
//
//
// export function axiosDelete(url: string, item: Object) {
//     return  axios.delete(firebaseUrl + url + "/" + item.id + ".json").then(() => {
//         //After deletion of to-buy item we create a historic record
//         if (url === 'groceryList') {
//             let itemBought = {
//                 id : new Date(),
//                 title : item.title,
//                 discount: item.discount,
//                 dateBought: new Date
//             }
//             axios.post(firebaseUrl + 'groceriesHist' + '.json', itemBought)
//         }
//         //After undoing of item hist, once again the item is placed in the to-buy list
//         if (url === 'groceryHist') {
//             let itemToBuy = {
//                 id : new Date(),
//                 title : item.title,
//                 discount: item.discount,
//                 constant: false
//             }
//             axios.post(firebaseUrl + 'groceryList' + '.json', itemToBuy)
//         }
//     })
// }
