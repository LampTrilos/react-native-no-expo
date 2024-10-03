import React from "react";
import {ScrollView, View} from "react-native";
import {Card, DataTable} from 'react-native-paper';

export default function SearchResults({style}) {
    const [page, setPage] = React.useState<number>(0);
    const [numberOfItemsPerPageList] = React.useState([10, 20, 30]);
    const [itemsPerPage, onItemsPerPageChange] = React.useState(
        numberOfItemsPerPageList[0]
    );

    const [items] = React.useState([
        {
            key: 1,
            name: 'Cupcake',
            calories: 356,
            fat: 16,
        },
        {
            key: 2,
            name: 'Eclair',
            calories: 262,
            fat: 16,
        },
        {
            key: 3,
            name: 'Frozen yogurt',
            calories: 159,
            fat: 6,
        },
        {
            key: 4,
            name: 'Gingerbread',
            calories: 305,
            fat: 3.7,
        },
        {
            key: 11,
            name: 'Cupcake',
            calories: 356,
            fat: 16,
        },
        {
            key: 12,
            name: 'Eclair',
            calories: 262,
            fat: 16,
        },
        {
            key: 13,
            name: 'Frozen yogurt',
            calories: 159,
            fat: 6,
        },
        {
            key: 145,
            name: 'Gingerbread',
            calories: 305,
            fat: 3.7,
        },
        {
            key: 444,
            name: 'Gingerbread',
            calories: 305,
            fat: 3.7,
        },
        {
            key: 114,
            name: 'Cupcake',
            calories: 356,
            fat: 16,
        },
        {
            key: 124,
            name: 'Eclair',
            calories: 262,
            fat: 16,
        },
        {
            key: 134,
            name: 'Frozen yogurt',
            calories: 159,
            fat: 6,
        },
        {
            key: 174,
            name: 'Gingerbread',
            calories: 305,
            fat: 3.7,
        },
    ]);

    const from = page * itemsPerPage;
    const to = Math.min((page + 1) * itemsPerPage, items.length);

    React.useEffect(() => {
        setPage(0);
    }, [itemsPerPage]);


    return (
        <View style={style}>
            <Card>
                <Card.Title title="EES Results" titleStyle={{fontSize: 17, fontWeight: 'bold'}}/>
                <Card.Content>
                    <ScrollView>
                            <DataTable>
                                <DataTable.Header>
                                    <DataTable.Title>Dessert</DataTable.Title>
                                    <DataTable.Title numeric>Calories</DataTable.Title>
                                    <DataTable.Title numeric>Fat</DataTable.Title>
                                </DataTable.Header>

                                {items.slice(from, to).map((item) => (
                                    <DataTable.Row key={item.key}>
                                        <DataTable.Cell>{item.name}</DataTable.Cell>
                                        <DataTable.Cell numeric>{item.calories}</DataTable.Cell>
                                        <DataTable.Cell numeric>{item.fat}</DataTable.Cell>
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
