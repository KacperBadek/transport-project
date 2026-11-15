export default interface JourneySearchRequest {
    originName: string;
    originLatitude: number;
    originLongitude: number;

    destinationName: string;
    destinationLatitude: number;
    destinationLongitude: number;

    departureTime: string;
}