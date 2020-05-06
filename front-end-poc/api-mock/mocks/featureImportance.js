const featureImportance = [];

const features = [
    'Monthly Tax Payment',
    'Monthly Insurance Payment',
    'Monthly HOA Payment',
    'Credit Score',
    'Down Payment',
    'Employment Income',
    'Other Income',
    'Assets',
    'Liabilities',
    'Lender Ratings'
]

for (let i = features.length - 1; i > 0; i--) {
    const j = Math.floor(Math.random() * i);
    const temp = features[i];
    features[i] = features[j];
    features[j] = temp;
}

for (let i = 0; i < 10; i++) {
    const feature = {
        featureName: features[i],
        featureScore: Math.random() * (Math.random() > 0.5 ? 1 : -1)
    };
    featureImportance.push(feature);
}

exports.featureImportance = featureImportance;