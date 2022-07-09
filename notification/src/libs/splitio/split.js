import { SplitFactory } from '@splitsoftware/splitio';
import { fileURLToPath } from 'url';
import path from 'path'

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const factory = SplitFactory({
  core: {
    authorizationKey: process.env['SPLIT_IO_KEY']
  },
  features: path.join(__dirname + '/../../../' + 'split.yml')
});

const splitClient = factory.client();

export { splitClient }
