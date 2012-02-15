exports.getSiteConfig = function () {
  configValues =  {
    url: 'http://localhost:3000',
    db: 'tcp://powerdns:powerdns2k10^^@plop:5433/powerdns',
    secret: 'padmin2k12^^'
  }
  return configValues;
}