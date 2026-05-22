package com.nammaraste.health.ui.viewmodels;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\bJ\u000e\u0010\u000e\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\bJ\u000e\u0010\u000f\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\bJ\u000e\u0010\u0010\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\bR\u001d\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2 = {"Lcom/nammaraste/health/ui/viewmodels/ReportViewModel;", "Landroidx/lifecycle/ViewModel;", "repository", "Lcom/nammaraste/health/data/ReportRepository;", "(Lcom/nammaraste/health/data/ReportRepository;)V", "allReports", "Lkotlinx/coroutines/flow/StateFlow;", "", "Lcom/nammaraste/health/data/ReportEntity;", "getAllReports", "()Lkotlinx/coroutines/flow/StateFlow;", "cycleSeverity", "Lkotlinx/coroutines/Job;", "report", "delete", "insert", "update", "app_debug"})
public final class ReportViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.nammaraste.health.data.ReportRepository repository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.nammaraste.health.data.ReportEntity>> allReports = null;
    
    public ReportViewModel(@org.jetbrains.annotations.NotNull()
    com.nammaraste.health.data.ReportRepository repository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.nammaraste.health.data.ReportEntity>> getAllReports() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job insert(@org.jetbrains.annotations.NotNull()
    com.nammaraste.health.data.ReportEntity report) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job update(@org.jetbrains.annotations.NotNull()
    com.nammaraste.health.data.ReportEntity report) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job delete(@org.jetbrains.annotations.NotNull()
    com.nammaraste.health.data.ReportEntity report) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job cycleSeverity(@org.jetbrains.annotations.NotNull()
    com.nammaraste.health.data.ReportEntity report) {
        return null;
    }
}