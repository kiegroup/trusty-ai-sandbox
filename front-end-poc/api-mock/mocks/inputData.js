const inputData = [
    {
        "inputName": "Credit Score",
        "typeRef": "number",
        "value": 738,
        "components": []
    },
    {
        "inputName": "Down Payment",
        "typeRef": "number",
        "value": 70000,
        "components": []
    },
    {
        "inputName": "Property",
        "typeRef": "tProperty",
        "value": null,
        "components": [
            {
                "inputName": "Purchase Price",
                "typeRef": "number",
                "value": 34000,
                "components": []
            },
            {
                "inputName": "Monthly Tax Payment",
                "typeRef": "number",
                "value": 0.2,
                "components": []
            },
            {
                "inputName": "Monthly Insurance Payment",
                "typeRef": "number",
                "value": 0.15,
                "components": []
            },
            {
                "inputName": "Monthly HOA Payment",
                "typeRef": "number",
                "value": 0.12,
                "components": []
            },
            {
                "inputName": "Address",
                "typeRef": "tAddress",
                "value": null,
                "components": [
                    {
                        "inputName": "Street",
                        "typeRef": "string",
                        "value": "272 10th St.",
                        "components": []
                    },
                    {
                        "inputName": "Unit",
                        "typeRef": "string",
                        "value": "A",
                        "components": []
                    },
                    {
                        "inputName": "City",
                        "typeRef": "string",
                        "value": "Malibu",
                        "components": []
                    },
                    {
                        "inputName": "State",
                        "typeRef": "string",
                        "value": "CA",
                        "components": []
                    },
                    {
                        "inputName": "ZIP",
                        "typeRef": "string",
                        "value": "90903",
                        "components": []
                    }
                ],
            }
        ],
    },
    {
        "inputName": "Borrower",
        "typeRef": "tBorrower",
        "value": null,
        "components": [
            {
                "inputName": "Full Name",
                "typeRef": "string",
                "value": "Jim Osterberg",
                "components": []
            },
            {
                "inputName": "Tax ID",
                "typeRef": "string",
                "value": "11123322323",
                "components": []
            },
            {
                "inputName": "Employment Income",
                "typeRef": "number",
                "value": 99000,
                "components": []
            },
            {
                "inputName": "Other Income",
                "typeRef": "number",
                "value": 0,
                "components": []
            },
            {
                "inputName": "Assets",
                "typeRef": "tAssets",
                "value": null,
                "components": [
                    [
                        {
                            inputName: "Type",
                            typeRef: "string",
                            value: "C",
                            components: []
                        },
                        {
                            inputName: "Institution Account or Description",
                            typeRef: "string",
                            value: "Chase",
                            components: []
                        },
                        {
                            inputName: "Value",
                            typeRef: "number",
                            value: 45000,
                            components: []
                        }
                    ],
                    [
                        {
                            inputName: "Type",
                            typeRef: "string",
                            value: "Other Non-Liquid",
                            components: []
                        },
                        {
                            inputName: "Institution Account or Description",
                            typeRef: "string",
                            value: "Vanguard",
                            components: []
                        },
                        {
                            inputName: "Value",
                            typeRef: "number",
                            value: 33000,
                            components: []
                        }
                    ]
                ]
            }
        ],
    },
    {
        "inputName": "Liabilities",
        "typeRef": "tLiabilities",
        "value": null,
        "components": [
            [
                {
                    inputName: "Type",
                    value: "Credit Card",
                    typeRef: "string",
                    components: []
                },
                {
                    inputName: "Payee",
                    value: "Chase",
                    typeRef: "string",
                    components: []
                },
                {
                    inputName: "Monthly Payment",
                    value: 300,
                    typeRef: "number",
                    components: []
                },
                {
                    inputName: "Balance",
                    value: 0,
                    typeRef: "number",
                    components: []
                },
                {
                    inputName: "To be paid off",
                    value: "Yes",
                    typeRef: "string",
                    components: []
                }
            ],
            [
                {
                    inputName: "Type",
                    value: "Lease",
                    typeRef: "string",
                    components: []
                },
                {
                    inputName: "Payee",
                    value: "BMW Finance",
                    typeRef: "string",
                    components: []
                },
                {
                    inputName: "Monthly Payment",
                    value: 450,
                    typeRef: "number",
                    components: []
                },
                {
                    inputName: "Balance",
                    value: 0,
                    typeRef: "number",
                    components: []
                },
                {
                    inputName: "To be paid off",
                    value: "No",
                    typeRef: "string",
                    components: []
                }
            ]
        ]
    },
    {
        "inputName": "Lender Ratings",
        "typeRef": "tLenderRatings",
        "value": null,
        "components": [
            [
                {
                    inputName: "Lender Name",
                    value: "Gordon Cole",
                    typeRef: "string",
                    components: []
                },
                {
                    inputName: "Customer Rating",
                    value: 4.2,
                    typeRef: "number",
                    components: []
                }
            ],
            [
                {
                    inputName: "Lender Name",
                    value: "Dale Cooper",
                    typeRef: "string",
                    components: []
                },
                {
                    inputName: "Customer Rating",
                    value: 3.6,
                    typeRef: "number",
                    components: []
                }
            ],
            [
                {
                    inputName: "Lender Name",
                    value: "Chester Desmond",
                    typeRef: "string",
                    components: []
                },
                {
                    inputName: "Customer Rating",
                    value: 4.6,
                    typeRef: "number",
                    components: []
                }
            ]
        ]
    }
];

exports.inputs = inputData;