const loanProductsNestedInputREST = [
    {
        inputLabel: "Credit Score",
        inputValue: 738
    },
    {
        inputLabel: "Down Payment",
        inputValue: 70000
    },
    {
        inputLabel: "Property",
        inputValue: [
            {
                inputLabel: "Purchase Price",
                inputValue: 340000
            },
            {
                inputLabel: "Monthly Tax Payment",
                inputValue: 350
            },
            {
                inputLabel: "Monthly Insurance Payment",
                inputValue: 100
            },
            {
                inputLabel: "Monthly HOA Payment",
                inputValue: 0
            },
            {
                inputLabel: "Address",
                inputValue: [
                    {
                        inputLabel: "Street",
                        inputValue: "272 10th St."
                    },
                    {
                        inputLabel: "Unit",
                        inputValue: null
                    },
                    {
                        inputLabel: "City",
                        inputValue: "Marina"
                    },
                    {
                        inputLabel: "State",
                        inputValue: "CA"
                    },
                    {
                        inputLabel: "ZIP",
                        inputValue: 93933
                    }
                ]
            },
        ]
    },
    {
        inputLabel: "Borrower",
        inputValue: [
            {
                inputLabel: "Full Name",
                inputValue: "Jim Osterberg"
            },
            {
                inputLabel: "Tax ID",
                inputValue: 1112223232
            },
            {
                inputLabel: "Employement Income",
                inputValue: 20000
            },
            {
                inputLabel: "Other Income",
                inputValue: 0
            },
            {
                inputLabel: "Assets",
                inputValue: {
                    itemsList: [
                        {
                            itemProperties: [
                                {
                                    inputLabel: "Type",
                                    inputValue: "c"
                                },
                                {
                                    inputLabel: "Institution Account or Description",
                                    inputValue: "Chase"
                                },
                                {
                                    inputLabel: "Value",
                                    inputValue: 35000
                                }
                            ]
                        },
                        {
                            itemProperties: [
                                {
                                    inputLabel: "Type",
                                    inputValue: "Other Non-Liquid"
                                },
                                {
                                    inputLabel: "Institution Account or Description",
                                    inputValue: "Vanguard"
                                },
                                {
                                    inputLabel: "Value",
                                    inputValue: 45000
                                }
                            ]
                        },
                        {
                            itemProperties: [
                                {
                                    inputLabel: "Type",
                                    inputValue: "Other Non-Liquid"
                                },
                                {
                                    inputLabel: "Institution Account or Description",
                                    inputValue: "Chase"
                                },
                                {
                                    inputLabel: "Value",
                                    inputValue: 17000
                                }
                            ]
                        },

                    ]
                }
            },
        ]
    },
    {
        inputLabel: "Liabilities",
        inputValue: {
            itemsList: [
                {
                    itemProperties: [
                        {
                            inputLabel: "Type",
                            inputValue: "Credit Card"
                        },
                        {
                            inputLabel: "Payee",
                            inputValue: "Chase"
                        },
                        {
                            inputLabel: "Monthly Payment",
                            inputValue: 300
                        },
                        {
                            inputLabel: "Balance",
                            inputValue: 0
                        },
                        {
                            inputLabel: "To be paid off",
                            inputValue: false
                        }
                    ]
                },
                {
                    itemProperties: [
                        {
                            inputLabel: "Type",
                            inputValue: "Lease"
                        },
                        {
                            inputLabel: "Payee",
                            inputValue: "BMW Finance"
                        },
                        {
                            inputLabel: "Monthly Payment",
                            inputValue: 450
                        },
                        {
                            inputLabel: "Balance",
                            inputValue: 0
                        },
                        {
                            inputLabel: "To be paid off",
                            inputValue: false
                        }
                    ]
                },
                {
                    itemProperties: [
                        {
                            inputLabel: "Type",
                            inputValue: "Alimony child support"
                        },
                        {
                            inputLabel: "Payee",
                            inputValue: null
                        },
                        {
                            inputLabel: "Monthly Payment",
                            inputValue: 1000
                        },
                        {
                            inputLabel: "Balance",
                            inputValue: 0
                        },
                        {
                            inputLabel: "To be paid off",
                            inputValue: false
                        }
                    ]
                },
                {
                    itemProperties: [
                        {
                            inputLabel: "Type",
                            inputValue: "Lien"
                        },
                        {
                            inputLabel: "Payee",
                            inputValue: "LA County"
                        },
                        {
                            inputLabel: "Monthly Payment",
                            inputValue: 100
                        },
                        {
                            inputLabel: "Balance",
                            inputValue: 0
                        },
                        {
                            inputLabel: "To be paid off",
                            inputValue: false
                        }
                    ]
                }

            ]
        }
    },
    {
        inputLabel: "Lender Ratings",
        inputValue: {
            itemsList: [
                {
                    itemProperties: [
                        {
                            inputLabel: "Lender Name",
                            inputValue: "Gordon Cole"
                        },
                        {
                            inputLabel: "Customer Rating",
                            inputValue: 4.2
                        }
                    ]
                },
                {
                    itemProperties: [
                        {
                            inputLabel: "Lender Name",
                            inputValue: "Dale Cooper"
                        },
                        {
                            inputLabel: "Customer Rating",
                            inputValue: 3.6
                        }
                    ]
                },
                {
                    itemProperties: [
                        {
                            inputLabel: "Lender Name",
                            inputValue: "Phillip Jeffries"
                        },
                        {
                            inputLabel: "Customer Rating",
                            inputValue: 4.9
                        }
                    ]
                },
                {
                    itemProperties: [
                        {
                            inputLabel: "Lender Name",
                            inputValue: "Chester Desmond"
                        },
                        {
                            inputLabel: "Customer Rating",
                            inputValue: 4.05
                        }
                    ]
                },

            ]
        }
    },

];

const loanProductsInput = {
    data: {
        creditScore: {
            label: "Credit Score",
            value: 738,
            impact: true
        },
        downPayment: {
            label: "Down Payment",
            value: 70000,
            impact: true
        },
        property: {
            label: "Property",
            children: {
                purchasePrice: {
                    label: "Purchase Price",
                    value: 34000,
                    impact: true
                },
                monthlyTaxPayment: {
                    label: "Monthly Tax Payment",
                    value: 2000,
                    impact: true
                },
                monthlyInsurancePayment: {
                    label: "Monthly Insurance Payment",
                    value: 200,
                    impact: true
                },
                monthlyHoaPayment: {
                    label: "Monthly HOA Payment",
                    value: 120
                },
                address: {
                    label: "Address",
                    children: {
                        street: {
                            label: "Street",
                            value: "272 10th St."
                        },
                        unit: {
                            label: "Unit",
                            value: "A"
                        },
                        city: {
                            label: "City",
                            value: "Malibu",
                            impact: true
                        },
                        state: {
                            label: "State",
                            value: "CA",
                            impact: true
                        },
                        zip: {
                            label: "ZIP",
                            value: 93933
                        }
                    }
                }
            }
        },
        borrower: {
            label: "Borrower",
            children: {
                name: {
                    label: "Full Name",
                    value: "Jim Osterberg",
                    impact: true
                },
                taxId: {
                    label: "Tax ID",
                    value: "11123322323"
                },
                employmentIncome: {
                    label: "Employment Income",
                    value: 99000,
                    impact: true
                },
                otherIncome: {
                    label: "Other Income",
                    value: 0
                },
                assets: {
                    label: "Assets",
                    list: [
                        {
                            type: {
                                label: "Type",
                                value: "C"
                            },
                            institutionAccount: {
                                label: "Institution Account or Description",
                                value: "Chase",
                                impact: true
                            },
                            value: {
                                label: "Value",
                                value: 45000,
                                impact: true
                            }
                        },
                        {
                            type: {
                                label: "Type",
                                value: "Other Non-Liquid"
                            },
                            institutionAccount: {
                                label: "Institution Account or Description",
                                value: "Vanguard"
                            },
                            value: {
                                label: "Value",
                                value: 33000,
                                impact: true
                            }
                        }
                    ]
                },
            }
        },
        liabilities: {
            label: "Liabilites",
            list: [
                {
                    type: {
                        label: "Type",
                        value: "Credit Card"
                    },
                    payee: {
                        label: "Payee",
                        value: "Chase"
                    },
                    monthlyPayment: {
                        label: "Monthly Payment",
                        value: 300,
                        impact: true
                    },
                    balance: {
                        label: "Balance",
                        value: 0,
                        impact: true
                    },
                    toBePaiedOff: {
                        label: "To be paid off",
                        value: "Yes"
                    }
                },
                {
                    type: {
                        label: "Type",
                        value: "Lease"
                    },
                    payee: {
                        label: "Payee",
                        value: "BMW Finance",
                        impact: true
                    },
                    monthlyPayment: {
                        label: "Monthly Payment",
                        value: 450,
                        impact: true
                    },
                    balance: {
                        label: "Balance",
                        value: 0
                    },
                    toBePaiedOff: {
                        label: "To be paid off",
                        value: "No"
                    }
                }
            ]
        },
        lenderRatings: {
            label: "Lender Ratings",
            list: [
                {
                    lenderName: {
                        label: "Lender Name",
                        value: "Gordon Cole"
                    },
                    customerRating: {
                        label: "Customer Rating",
                        value: 4.2,
                        impact: true
                    }
                },
                {
                    lenderName: {
                        label: "Lender Name",
                        value: "Dale Cooper"
                    },
                    customerRating: {
                        label: "Customer Rating",
                        value: 3.6,
                        impact: true
                    }
                },
                {
                    lenderName: {
                        label: "Lender Name",
                        value: "Chester Desmond"
                    },
                    customerRating: {
                        label: "Customer Rating",
                        value: 4.06,
                        impact: true
                    }
                }
            ]
        }
    }
};

export { loanProductsNestedInputREST };
export default loanProductsInput;