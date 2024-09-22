import axios from 'axios';

const firebaseUrl = 'https://test-f94ee-default-rtdb.europe-west1.firebasedatabase.app/';

export function axiosGet(url: string, timeout: number) {
    return  axios.get(firebaseUrl + url + '.json', {
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
