import { example1 } from "./example";
import { document } from "./generated/www.dmg.org/PMML-4_4";
import { LinearRegressionHandler } from "./LinearRegressionHandler";
import { withCXML } from "./unmarshall/unmarshaller";

export { withCXML as unmarshall, document as PMMLDocument, example1, LinearRegressionHandler };
