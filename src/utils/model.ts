//This file contains the models for the various object that are used in the frontend
//------------------------The loggedIn user------------------------//
export interface User {
    fullName: string,
    userId: string;
    //Vardia of the current user
    shift: string;
}

export class UserClass implements User {
    constructor(
        public fullName: string,
        public userId: string,
        //Vardia of the current user
        public shift: string,
    ) {}
}
//------------------------End of the loggedIn user------------------------//
//--------------The business flow of the PassportCheck procedure------------------------//
//The allowed statuses
export enum ControlStatusTypes {
    None = "None",
    PassportPhoto = "PassportPhoto",
    PassportNFC = "PassportNFC",
    Photo = "Photo",
    Finger = "Finger",
}
//--------------End of the business flow of the PassportCheck procedure------------------------//
