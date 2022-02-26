package vo;

public enum Permission {
	INVALID,
	GUEST,
	NEWBIE,
	YB,
	OB,
	YB_ADMIN,
	OB_ADMIN,
	GENREAL_ADMIN,
	YB_OB_ADMIN;
	
	public static Permission intToPermission(int permission){
		switch(permission) {
		case 0:
			return GUEST;
		case 1:
			return NEWBIE; 
		case 2: 
			return YB;
		case 3:
			return OB;
		case 4:
			return YB_ADMIN;
		case 5:
			return OB_ADMIN;
		case 6:
			return GENREAL_ADMIN;
		case 7:
			return YB_OB_ADMIN;
		default:
			return INVALID;
		}
	}
	
	public static int permissionToInt(Permission permission){
		switch(permission) {
		case GUEST:
			return 0;
		case NEWBIE:
			return 1; 
		case YB: 
			return 2;
		case OB:
			return 3;
		case YB_ADMIN:
			return 4;
		case OB_ADMIN:
			return 5;
		case GENREAL_ADMIN:
			return 6;
		case YB_OB_ADMIN:
			return 7;
		default:
			return -1;
		}
	}
}
