package api.bundesliga.entity;

public enum SeasonStatus {
    NOT_STARTED, STARTED, FINISHED;

    public boolean canTransitionTo(SeasonStatus next) {
        return this.ordinal() + 1 == next.ordinal();
    }
}
