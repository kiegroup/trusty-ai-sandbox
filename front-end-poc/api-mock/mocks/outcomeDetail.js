const outcomeDetail = {
    evaluationStatus: "EVALUATED",
    hasErrors: false,
    messages: [],
    outcomeId: "432343443",
    outcomeName: "Client Ratings",
    outcomeResult: {
        name: "Client Ratings",
        type: "tProducts",
        value: null,
        components: [
            {
                name: "Rating Type A",
                value: null,
                typeRef: "string",
                components: [
                    {
                        name: "Loan Amount",
                        value: 540000,
                        typeRef: "number",
                        components: null
                    },
                    {
                        name: "Repayment Rate",
                        value: 900,
                        typeRef: "number",
                        components: null
                    },
                    {
                        name: "Loan Eligibility",
                        value: true,
                        typeRef: "boolean",
                        components: null
                    }
                ]
            },
            {
                name: "Rating Type B",
                value: null,
                typeRef: "number",
                components: [
                    {
                        name: "Loan amount",
                        value: 340000,
                        typeRef: "number",
                        components: null
                    },
                    {
                        name: "Repayment rate",
                        value: 2000,
                        typeRef: "number",
                        components: null
                    },
                    {
                        name: "Sub-Rating Type C",
                        value: null,
                        typeRef: "number",
                        components: [
                            {
                                name: "Loan amount",
                                value: 340000,
                                typeRef: "number",
                                components: null
                            },
                            {
                                name: "Repayment rate",
                                value: 2000,
                                typeRef: "number",
                                components: null
                            }
                        ]
                    }

                ]
            }
        ]
    },
    outcomeInputs: [
        {
            "name": "Credit Score",
            "typeRef": "number",
            "value": 738,
            "components": []
        },
        {
            "name": "Down Payment",
            "typeRef": "number",
            "value": 70000,
            "components": []
        },
        {
            "name": "Property",
            "typeRef": "tProperty",
            "value": null,
            "components": [
                {
                    "name": "Purchase Price",
                    "typeRef": "number",
                    "value": 34000,
                    "components": []
                },
                {
                    "name": "Monthly Tax Payment",
                    "typeRef": "number",
                    "value": 0.2,
                    "components": []
                },
                {
                    "name": "Monthly Insurance Payment",
                    "typeRef": "number",
                    "value": 0.15,
                    "components": []
                },
                {
                    "name": "Monthly HOA Payment",
                    "typeRef": "number",
                    "value": 0.12,
                    "components": []
                },
                {
                    "name": "Address",
                    "typeRef": "tAddress",
                    "value": null,
                    "components": [
                        {
                            "name": "Street",
                            "typeRef": "string",
                            "value": "272 10th St.",
                            "components": []
                        },
                        {
                            "name": "Unit",
                            "typeRef": "string",
                            "value": "A",
                            "components": []
                        },
                        {
                            "name": "City",
                            "typeRef": "string",
                            "value": "Malibu",
                            "components": []
                        },
                        {
                            "name": "State",
                            "typeRef": "string",
                            "value": "CA",
                            "components": []
                        },
                        {
                            "name": "ZIP",
                            "typeRef": "string",
                            "value": "90903",
                            "components": []
                        }
                    ],
                }
            ],
        },
        {
            "name": "Lender Ratings",
            "typeRef": "tLenderRatings",
            "value": null,
            "components": [
                [
                    {
                        name: "Lender Name",
                        value: "Gordon Cole",
                        typeRef: "string",
                        components: []
                    },
                    {
                        name: "Customer Rating",
                        value: 4.2,
                        typeRef: "number",
                        components: []
                    }
                ],
                [
                    {
                        name: "Lender Name",
                        value: "Dale Cooper",
                        typeRef: "string",
                        components: []
                    },
                    {
                        name: "Customer Rating",
                        value: 3.6,
                        typeRef: "number",
                        components: []
                    }
                ],
                [
                    {
                        name: "Lender Name",
                        value: "Chester Desmond",
                        typeRef: "string",
                        components: []
                    },
                    {
                        name: "Customer Rating",
                        value: 4.6,
                        typeRef: "number",
                        components: []
                    }
                ]
            ]
        }
    ]
}

module.exports = outcomeDetail;