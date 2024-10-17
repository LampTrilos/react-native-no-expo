//Contains all the axios call that are needed by the application
import customAxios from "./customAxios"
import {LoginCredentials, newUser} from "./model.ts";
import AsyncStorage from '@react-native-async-storage/async-storage';


//The base url of the backend
const baseUrl = 'https://XXXXYYYYY/';

//---------------Log in section and token persistence---------------------------//
//Log in
export async function login(credentials: LoginCredentials) {
    // return  await customAxios.post(baseUrl + 'PLACEHOLDER', credentials).then((res) => {
    //     //Store the token on storage if the call is successfull
    //     storeToken(res.token)
    // })
    return newUser('Telikos user', '234532', '08:00 - 19:00');
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

//Search in EES
export async function eesSearch() {
    return  await customAxios.get(baseUrl + url + '.json', {
        timeout: timeout // timeout in milliseconds (5000ms = 5s)
    })
}

export function axiosPost(url: string, item: Object) {
    return  axios.post(firebaseUrl + url + '.json', item)
}

export function axiosPut(url: string, item: Object) {
    //console.log(firebaseUrl + "/" + item.id +  '.json')
    return  axios.put(firebaseUrl + url + "/"  + item.id +  '.json', item)
}


export function axiosDelete(url: string, item: Object) {
    return  axios.delete(firebaseUrl + url + "/" + item.id + ".json").then(() => {
        //After deletion of to-buy item we create a historic record
        if (url === 'groceryList') {
            let itemBought = {
                id : new Date(),
                title : item.title,
                discount: item.discount,
                dateBought: new Date
            }
            axios.post(firebaseUrl + 'groceriesHist' + '.json', itemBought)
        }
        //After undoing of item hist, once again the item is placed in the to-buy list
        if (url === 'groceryHist') {
            let itemToBuy = {
                id : new Date(),
                title : item.title,
                discount: item.discount,
                constant: false
            }
            axios.post(firebaseUrl + 'groceryList' + '.json', itemToBuy)
        }
    })
}
