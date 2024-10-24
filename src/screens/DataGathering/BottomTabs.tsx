import {BottomNavigation, Text} from "react-native-paper";
import React, {useEffect} from "react";
import PassportCheck from "./PassportCheck/PassportCheck.tsx";
import SearchResults from "./SearchResults/SearchResults.tsx";
import FaceCapture from "../FaceCapture.tsx";
import LeftRightButtonsLayout from "../../components/LeftRightButtonsLayout"


export default function BottomTabs() {
    //This sets the first page of the tabs of the check
    const [index, setIndex] = React.useState(1);
    //Run only once when the component renders
    useEffect(() => {
        console.log('Bottom tabs is beginning..')
    }, []);


    //Bottom navigation attributes
    const PassportRoute = () => <PassportCheck/>;
    const PhotoRoute = () => <LeftRightButtonsLayout><FaceCapture /></LeftRightButtonsLayout>;
    const BiometricsRoute = () => <FaceCapture/>;
    const ResultsRoute = () => <SearchResults/>;


    const routes = [
        {key: 'Passport', title: 'Passport', focusedIcon: 'passport-biometric',},
        {key: 'Photo', title: 'Photo', focusedIcon: 'camera'},
        {key: 'Biometrics', title: 'Biometrics', focusedIcon: 'fingerprint'},
        {key: 'Results', title: 'Results', focusedIcon: 'account-search'},
    ];

    const renderScene = BottomNavigation.SceneMap({
        Passport: PassportRoute,
        Biometrics: BiometricsRoute,
        Photo: PhotoRoute,
        Results: ResultsRoute,
    });

    //-----------------End of Bottom navigation routes------------------//
    return (
        <BottomNavigation
            navigationState={{index, routes}}
            onIndexChange={() => {}} //We don't want the user to change tabs at will, only with controlled actions
            //onIndexChange={setIndex}
            renderScene={renderScene}

        />
    )
}
