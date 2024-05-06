DROP TABLE IF EXISTS <tableName>;
--SPLIT
CREATE TABLE IF NOT EXISTS <tableName> (
    OBJECTID INTEGER,
    advisorySpeed INTEGER,
    bicycle INTEGER,
    bridge INTEGER,
    bus INTEGER,
    carStationWagon INTEGER,
    cliffBank INTEGER,
    crashDirectionDescription TEXT,
    crashFinancialYear VARCHAR(10),
    crashLocationOne TEXT,
    crashLocationTwo TEXT,
    crashRoadSideRoad INTEGER,
    crashSeverity TEXT,
    crashSHDescription VARCHAR(3),
    crashYear INTEGER,
    debris INTEGER,
    directionRoleDescription VARCHAR(5),
    ditch INTEGER,
    fatalCount INTEGER,
    fence INTEGER,
    flatHill VARCHAR (10),
	guardRail INTEGER,
	holiday TEXT,
	houseOrBuilding INTEGER,
	intersection TEXT,
	kerb INTEGER,
	light TEXT,
	minorInjuryCount INTEGER,
	moped INTEGER,
	motorcycle INTEGER,
	numberOfLanes INTEGER,
	objectThrownOrDropped INTEGER,
	otherObject INTEGER,
	otherVehicleType INTEGER,
	overBank INTEGER,
	parkedVehicle INTEGER,
	pedestrian INTEGER,
	phoneBoxEtc INTEGER,
	postOrPole INTEGER,
	region TEXT,
	roadCharacter TEXT,
	roadLane VARCHAR(10),
	roadSurface VARCHAR(8),
	roadworks INTEGER,
	schoolBus INTEGER,
	seriousInjuryCount INTEGER,
	slipOrFlood INTEGER,
	speedLimit INTEGER,
	strayAnimal INTEGER,
	streetLight VARCHAR(10),
	suv INTEGER,
	taxi INTEGER,
	temporarySpeedLimit INTEGER,
	tlaName TEXT,
	trafficControl TEXT,
	trafficIsland INTEGER,
	trafficSign INTEGER,
	train INTEGER,
	tree INTEGER,
	truck INTEGER,
	unknownVehicleType INTEGER,
	urban VARCHAR(10),
	vanOrUtility INTEGER,
	vehicle INTEGER,
	waterRiver INTEGER,
	weatherA VARCHAR(15),
	weatherB VARCHAR(15),
    lat REAL,
    lon REAL,
    isCustom INTEGER,
    UNIQUE (OBJECTID, isCustom)
    ) ;
