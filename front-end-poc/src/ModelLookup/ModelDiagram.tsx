import React from 'react';
import { IModelVersion } from "./types"

const ModelDiagram = (props: { selectedVersion: IModelVersion }) => {
    const { selectedVersion } = props;
    const editorUrl = "https://kiegroup.github.io/kogito-online/?file=https://raw.githubusercontent.com/kiegroup/kogito-tooling/master/packages/online-editor/static/samples/sample.dmn#/editor/dmn";
    const kogitoIframe = () => {
        return { __html: `<iframe src=${editorUrl} data-key="${selectedVersion.version}"></iframe>` };
    };

    return (
        <div className="model-diagram">
            <div className="model-diagram__iframe" dangerouslySetInnerHTML={kogitoIframe()} />
        </div>
    );
};

export default ModelDiagram;