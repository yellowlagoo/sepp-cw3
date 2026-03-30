package Controller; 

public abstract class Controller{
    private User currentUser; //TODO: make the packages work 


    public Controller(User currentUser) {
        this.currentUser = currentUser;
    }

    /**
     * Checks whether or not the current user is a guest, meaning they are not logged in yet
     * @return - whether or not the current user is a guest
     */
    protected boolean checkCurrentUserIsGuest(){
        return currentUser.isLoggedIn() == false;
    }

    /**
     * Checks whether or not the current user is an admin staff
     * @return - whether or not the current user is an admin staff
     */
    protected boolean checkCurrentUserIsAdmin(){
        return currentUser instanceof AdminStaff;
    }

    /**
     * Checks whether or not the current user is a student
     * @return - whether or not the current user is a student
     */
    protected boolean checkCurrentUserIsStudent(){
        return currentUser instanceof Student;
    }

    /**
     * Checks whether or not the current user is an entertainment provider
     * @return - whether or not the current user is an entertainment provider
     */
    protected boolean checkCurrentUserIsEntertainmentProvider(){
        return currentUser instanceof EntertainmentProvider;
    }

    //TODO: add selectFromMenu
}