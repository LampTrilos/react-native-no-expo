import React from "react";
import {ScrollView, View} from "react-native";
import {Card, DataTable} from 'react-native-paper';
import {GenderTypes, newPoliceCheckResult} from "../../../utils/model.ts";

export default function SearchResults({style}) {
    const [page, setPage] = React.useState<number>(0);
    //List of paging options, apparently setting an outrageous numbers disables the paginator, which is fine
    const [numberOfItemsPerPageList] = React.useState([500]);
    const [itemsPerPage, onItemsPerPageChange] = React.useState(
        numberOfItemsPerPageList[0]
    );

    const [items] = React.useState([
        newPoliceCheckResult("1", null, 'Test1', 'Petros', 'ΓΣΔ', '15/09/2021', GenderTypes.MALE, 'Οπλοφορία' ),
        newPoliceCheckResult("2", null, 'Test2', 'Petros', 'GRC', '15/09/2021', GenderTypes.FEMALE, 'Οπλοφορία1' ),
        newPoliceCheckResult("3", null, 'Test3', 'Petros', 'ΞΒΔ', '15/09/2021', GenderTypes.MALE, 'Οπλοφορία' ),
        newPoliceCheckResult("4", null, 'Test4', 'Petros', 'GRC', '15/09/2021', GenderTypes.MALE, 'Οπλοφορία3' ),
        newPoliceCheckResult("5", null, 'Test5', 'Petros', 'ΦΤΔ', '15/09/2021', GenderTypes.FEMALE, 'Οπλοφορία4' )
    ]);

    const from = page * itemsPerPage;
    const to = Math.min((page + 1) * itemsPerPage, items.length);

    React.useEffect(() => {
        setPage(0);
    }, [itemsPerPage]);


    return (
        <View style={style}>
            <Card >
                <Card.Title title="EES Results" titleStyle={{fontSize: 17, fontWeight: 'bold'}}/>
                <Card.Content>
                    <ScrollView>
                            <DataTable>
                                <DataTable.Header>
                                    <DataTable.Title>Επώνυμο</DataTable.Title>
                                    <DataTable.Title numeric>Όνομα</DataTable.Title>
                                    <DataTable.Title numeric>Αδίκημα</DataTable.Title>
                                </DataTable.Header>

                                {items.slice(from, to).map((item) => (
                                    <DataTable.Row key={item.id}>
                                        <DataTable.Cell>{item.familyName}</DataTable.Cell>
                                        <DataTable.Cell numeric>{item.firstName}</DataTable.Cell>
                                        <DataTable.Cell numeric>{item.crime}</DataTable.Cell>
                                    </DataTable.Row>
                                ))}

                                <DataTable.Pagination
                                    page={page}
                                    numberOfPages={Math.ceil(items.length / itemsPerPage)}
                                    onPageChange={(page) => setPage(page)}
                                    label={`${from + 1}-${to} of ${items.length}`}
                                    numberOfItemsPerPageList={numberOfItemsPerPageList}
                                    numberOfItemsPerPage={itemsPerPage}
                                    onItemsPerPageChange={onItemsPerPageChange}
                                    showFastPaginationControls
                                    selectPageDropdownLabel={'Rows per page'}
                                />
                            </DataTable>
                        </ScrollView>
                </Card.Content>
            </Card>
        </View>
    );
};
