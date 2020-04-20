const loanProductsInputScores = {
    creditScore: {
        label: "Credit Score",
        value: 738,
        score: 0.35
    },
    downPayment: {
        label: "Down Payment",
        value: 70000,
        score: 0.26
    },
    property: {
        label: "Property",
        children: {
            purchasePrice: {
                label: "Purchase Price",
                value: 34000,
                score: 0.22
            },
            monthlyTaxPayment: {
                label: "Monthly Tax Payment",
                value: 2000,
                score: 0.20
            },
            monthlyInsurancePayment: {
                label: "Monthly Insurance Payment",
                value: 200,
                score: 0.15
            },
            monthlyHoaPayment: {
                label: "Monthly HOA Payment",
                value: 120,
                score: 0.12
            },
            address: {
                label: "Address",
                children: {
                    street: {
                        label: "Street",
                        value: "272 10th St.",
                        score: 0
                    },
                    unit: {
                        label: "Unit",
                        value: "A",
                        score: 0
                    },
                    city: {
                        label: "City",
                        value: "Malibu",
                        score: 0.06
                    },
                    state: {
                        label: "State",
                        value: "CA",
                        score: 0.02
                    },
                    zip: {
                        label: "ZIP",
                        value: 93933,
                        score: 0
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
                score: 0
            },
            taxId: {
                label: "Tax ID",
                value: "11123322323",
                score: 0
            },
            employmentIncome: {
                label: "Employment Income",
                value: 99000,
                score: 0.12
            },
            otherIncome: {
                label: "Other Income",
                value: 0,
                score: 0
            },
            assets: {
                label: "Assets",
                list: [
                    {
                        type: {
                            label: "Type",
                            value: "C",
                            score: 0
                        },
                        institutionAccount: {
                            label: "Institution Account or Description",
                            value: "Chase",
                            score: 0.04
                        },
                        value: {
                            label: "Value",
                            value: 45000,
                            score: 0.04
                        }
                    },
                    {
                        type: {
                            label: "Type",
                            value: "Other Non-Liquid",
                            score: 0
                        },
                        institutionAccount: {
                            label: "Institution Account or Description",
                            value: "Vanguard",
                            score: 0.2
                        },
                        value: {
                            label: "Value",
                            value: 33000,
                            score: 0.2
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
                    value: "Credit Card",
                    score: 0.08
                },
                payee: {
                    label: "Payee",
                    value: "Chase",
                    score: 0.06
                },
                monthlyPayment: {
                    label: "Monthly Payment",
                    value: 300,
                    score: 0.05
                },
                balance: {
                    label: "Balance",
                    value: 0,
                    score: 0.03
                },
                toBePaiedOff: {
                    label: "To be paid off",
                    value: "Yes",
                    score: 0.02
                }
            },
            {
                type: {
                    label: "Type",
                    value: "Lease",
                    score: 0.01
                },
                payee: {
                    label: "Payee",
                    value: "BMW Finance",
                    score: 0.03
                },
                monthlyPayment: {
                    label: "Monthly Payment",
                    value: 450,
                    score: 0.01
                },
                balance: {
                    label: "Balance",
                    value: 0,
                    score: 0.02
                },
                toBePaiedOff: {
                    label: "To be paid off",
                    value: "No",
                    score: 0
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
                    value: "Gordon Cole",
                    score: 0
                },
                customerRating: {
                    label: "Customer Rating",
                    value: 4.2,
                    score: 0
                }
            },
            {
                lenderName: {
                    label: "Lender Name",
                    value: "Dale Cooper",
                    score: 0
                },
                customerRating: {
                    label: "Customer Rating",
                    value: 3.6,
                    score: 0
                }
            },
            {
                lenderName: {
                    label: "Lender Name",
                    value: "Chester Desmond",
                    score: 0
                },
                customerRating: {
                    label: "Customer Rating",
                    value: 4.06,
                    score: 0
                }
            }
        ]
    }
};

exports.input = loanProductsInputScores;