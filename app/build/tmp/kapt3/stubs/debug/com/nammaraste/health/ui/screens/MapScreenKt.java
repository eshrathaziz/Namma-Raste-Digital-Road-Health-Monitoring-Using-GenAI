package com.nammaraste.health.ui.screens;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000T\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0018\u0002\n\u0000\u001a\"\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0007H\u0003\u001a:\u0010\b\u001a\u00020\u00012\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u0004\u001a\u00020\u00052\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00010\f2\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00010\u000eH\u0007\u001a\"\u0010\u000f\u001a\u00020\u00012\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0014\u0010\u0015\u001a\u0018\u0010\u0016\u001a\u00020\u00012\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0017\u001a\u00020\u0018H\u0007\u001a<\u0010\u0019\u001a\u00020\u00012\u0006\u0010\u001a\u001a\u00020\u001b2\b\u0010\u001c\u001a\u0004\u0018\u00010\u00132\u0006\u0010\u001d\u001a\u00020\u00132\u0006\u0010\u001e\u001a\u00020\u00132\u0006\u0010\u001f\u001a\u00020\u001b2\b\b\u0002\u0010\u0006\u001a\u00020\u0007H\u0003\u001a>\u0010 \u001a\u00020\u00012\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u0004\u001a\u00020\u00052\f\u0010!\u001a\b\u0012\u0004\u0012\u00020\u00010\f2\f\u0010\"\u001a\b\u0012\u0004\u0012\u00020\u00010\f2\b\b\u0002\u0010\u0006\u001a\u00020\u0007H\u0003\u001a%\u0010#\u001a\u00020\u00012\b\b\u0002\u0010\u0006\u001a\u00020\u00072\u0011\u0010$\u001a\r\u0012\u0004\u0012\u00020\u00010\f\u00a2\u0006\u0002\b%H\u0007\u0082\u0002\u0007\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006&"}, d2 = {"ContractorCard", "", "contractor", "Lcom/nammaraste/health/Contractor;", "isKannada", "", "modifier", "Landroidx/compose/ui/Modifier;", "EditReportDialog", "report", "Lcom/nammaraste/health/data/ReportEntity;", "onDismiss", "Lkotlin/Function0;", "onSave", "Lkotlin/Function1;", "LegendDot", "color", "Landroidx/compose/ui/graphics/Color;", "label", "", "LegendDot-DxMtmZc", "(JLjava/lang/String;)V", "MapScreen", "viewModel", "Lcom/nammaraste/health/ui/viewmodels/ReportViewModel;", "PodiumCard", "rank", "", "medal", "roadName", "zone", "health", "ReportLogItem", "onDelete", "onEdit", "ScrollableRow", "content", "Landroidx/compose/runtime/Composable;", "app_debug"})
public final class MapScreenKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void MapScreen(boolean isKannada, @org.jetbrains.annotations.NotNull()
    com.nammaraste.health.ui.viewmodels.ReportViewModel viewModel) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void EditReportDialog(@org.jetbrains.annotations.NotNull()
    com.nammaraste.health.data.ReportEntity report, boolean isKannada, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.nammaraste.health.data.ReportEntity, kotlin.Unit> onSave) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void ScrollableRow(@org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> content) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void PodiumCard(int rank, java.lang.String medal, java.lang.String roadName, java.lang.String zone, int health, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ContractorCard(com.nammaraste.health.Contractor contractor, boolean isKannada, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ReportLogItem(com.nammaraste.health.data.ReportEntity report, boolean isKannada, kotlin.jvm.functions.Function0<kotlin.Unit> onDelete, kotlin.jvm.functions.Function0<kotlin.Unit> onEdit, androidx.compose.ui.Modifier modifier) {
    }
}