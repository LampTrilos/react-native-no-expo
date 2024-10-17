import axios from 'axios';
import {LoginCredentials, newUser} from "./model.ts";
//Contains all the axios call that are needed by the application


const baseUrl = 'https://XXXXYYYYY/';

//Log in
export async function login(credentials: LoginCredentials) {
    //return  await axios.post(baseUrl + 'PLACEHOLDER', credentials)
    return newUser('Telikos user', '234532', '08:00 - 19:00');
}

//Search in EES
export async function eesSearch(url: string, timeout: number) {
    return  await axios.get(baseUrl + url + '.json', {
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
